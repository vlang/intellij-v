package org.vlang.ide.codeInsight.imports

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.daemon.impl.DaemonListeners
import com.intellij.codeInsight.daemon.impl.ShowAutoImportPass
import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInspection.HintAction
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.impl.LaterInvocator
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.ui.SimpleColoredComponent
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBList
import com.intellij.util.IncorrectOperationException
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.ui.JBUI
import org.vlang.ide.ui.VIcons
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.VlangTypeReferenceExpression
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.psi.impl.VlangReference
import org.vlang.lang.stubs.index.VlangModulesIndex

class VlangImportModuleQuickFix : LocalQuickFixAndIntentionActionOnPsiElement, HintAction, HighPriorityAction {
    private val myModuleName: String
    private var myModulesToImport: List<String>? = null

    constructor(element: PsiElement, importPath: String) : super(element) {
        myModuleName = ""
        myModulesToImport = listOf(importPath)
    }

    constructor(reference: PsiReference) : super(reference.element) {
        myModuleName = reference.canonicalText
    }

    fun getReference(element: PsiElement?): PsiReference? {
        if (element != null && element.isValid) {
            for (reference in element.references) {
                if (isSupportedReference(reference)) {
                    return reference
                }
            }
        }
        return null
    }

    override fun showHint(editor: Editor): Boolean {
        return doAutoImportOrShowHint(editor, true)
    }

    override fun getText(): String {
        val element = startElement
        return if (element != null) {
            "Import " + getText(getImportPathVariantsToImport(element))
        } else {
            "Import module"
        }
    }

    override fun getFamilyName(): String {
        return "Import module"
    }

    override operator fun invoke(
        project: Project, file: PsiFile, editor: Editor?,
        startElement: PsiElement, endElement: PsiElement,
    ) {
        if (!FileModificationService.getInstance().prepareFileForWrite(file)) return
        perform(getImportPathVariantsToImport(startElement), file, editor)
    }

    override fun isAvailable(
        project: Project,
        file: PsiFile,
        startElement: PsiElement,
        endElement: PsiElement,
    ): Boolean {
        val reference = getReference(startElement)
        return file is VlangFile && file.manager.isInProject(file) && reference != null && reference.resolve() == null && getImportPathVariantsToImport(
            startElement
        ).isNotEmpty() && notQualified(startElement)
    }

    private fun getImportPathVariantsToImport(element: PsiElement): List<String> {
        if (myModulesToImport == null) {
            myModulesToImport = getImportPathVariantsToImport(myModuleName, element)
        }
        return myModulesToImport!!
    }

    fun doAutoImportOrShowHint(editor: Editor, showHint: Boolean): Boolean {
        val element: PsiElement? = startElement
        if (element == null || !element.isValid) {
            return false
        }
        val reference = getReference(element)
        if (reference == null || reference.resolve() != null) {
            return false
        }

        val modulesToImport = getImportPathVariantsToImport(element)
        if (modulesToImport.isEmpty()) {
            return false
        }

        val file = element.containingFile
        val firstModuleToImport = ContainerUtil.getFirstItem(modulesToImport)

        // autoimport on trying to fix
        if (modulesToImport.size == 1) {
            if (VlangCodeInsightSettings.getInstance().isAddUnambiguousImportsOnTheFly && !LaterInvocator.isInModalContext() &&
                (ApplicationManager.getApplication().isUnitTestMode || DaemonListeners.canChangeFileSilently(file))
            ) {
                CommandProcessor.getInstance().runUndoTransparentAction { perform(file, firstModuleToImport) }
                return true
            }
        }

        // show hint on failed autoimport
        if (showHint) {
            if (ApplicationManager.getApplication().isUnitTestMode) return false
            if (HintManager.getInstance().hasShownHintsThatWillHideByOtherHint(true)) return false
            if (!VlangCodeInsightSettings.getInstance().isShowImportPopup) return false
            val referenceRange = reference.rangeInElement.shiftRight(element.textRange.startOffset)
            HintManager.getInstance().showQuestionHint(
                editor,
                ShowAutoImportPass.getMessage(modulesToImport.size > 1, ContainerUtil.getFirstItem(modulesToImport)),
                referenceRange.startOffset,
                referenceRange.endOffset
            ) {
                if (file.isValid && !editor.isDisposed) {
                    perform(modulesToImport, file, editor)
                }
                true
            }
            return true
        }
        return false
    }

    private fun perform(modulesToImport: List<String>, file: PsiFile, editor: Editor?) {
        LOG.assertTrue(
            editor != null || modulesToImport.size == 1,
            "Cannot invoke fix with ambiguous imports on null editor"
        )

        if (modulesToImport.size > 1 && editor != null) {
            val list = JBList(modulesToImport)

            list.installCellRenderer { name ->
                val parts = name.split('.')
                val shortName = parts.last()

                SimpleColoredComponent().apply {
                    icon = VIcons.Module
                    append(shortName)
                    append(" ($name)", SimpleTextAttributes.GRAY_ATTRIBUTES)
                    border = JBUI.Borders.empty(2, 4, 2, 4)
                }
            }

            val builder = JBPopupFactory.getInstance().createListPopupBuilder(list).setRequestFocus(true)
                .setTitle("Module to Import")
                .setItemChoosenCallback {
                    val i = list.selectedIndex
                    if (i < 0) {
                        return@setItemChoosenCallback
                    }

                    perform(file, modulesToImport[i])
                }
                .setFilteringEnabled { element: Any -> if (element is String) element else element.toString() }

            val popup = builder.createPopup()
            builder.scrollPane.border = null
            builder.scrollPane.viewportBorder = null
            popup.showInBestPositionFor(editor)
        } else if (modulesToImport.size == 1) {
            perform(file, modulesToImport.first())
        } else {
            val modules = modulesToImport.joinToString(", ")
            throw IncorrectOperationException("Cannot invoke fix with ambiguous imports on editor ()$editor. Modules: $modules")
        }
    }

    private fun perform(file: PsiFile, pathToImport: String?) {
        if (file !is VlangFile || pathToImport == null) {
            return
        }

        val project = file.project

        CommandProcessor.getInstance().executeCommand(project, {
            runWriteAction {
                if (!isAvailable) {
                    return@runWriteAction
                }

                if (file.getImportedModulesMap().containsKey(pathToImport)) {
                    return@runWriteAction
                }
                file.addImport(pathToImport, null)
            }
        }, "Add Import", null)
    }

//    private class MyImportsComparator(context: PsiElement?, vendoringEnabled: Boolean) : Comparator<String> {
//        private val myContextImportPath: String?
//
//        init {
//            myContextImportPath = VlangCompletionUtil.getContextImportPath(context, vendoringEnabled)
//        }
//
//        override fun compare(s1: String, s2: String): Int {
//            val result: Int = Comparing.compare(
//                VlangCompletionUtil.calculateModulePriority(s2, myContextImportPath),
//                VlangCompletionUtil.calculateModulePriority(s1, myContextImportPath)
//            )
//            return if (result != 0) result else Comparing.compare(s1, s2)
//        }
//    }

    companion object {
        private fun isSupportedReference(reference: PsiReference?): Boolean {
            return reference is VlangReference
        }

        private fun getText(modulesToImport: Collection<String>): String {
            return ContainerUtil.getFirstItem(modulesToImport, "") + "? " + if (modulesToImport.size > 1) "(multiple choices...) " else ""
        }

        private fun notQualified(startElement: PsiElement?): Boolean {
            return startElement is VlangReferenceExpression && startElement.getQualifier() == null ||
                    startElement is VlangTypeReferenceExpression && startElement.getQualifier() == null
        }

        fun getImportPathVariantsToImport(moduleName: String, context: PsiElement): List<String> {
            val contextFile = context.containingFile
            val imported =
                if (contextFile is VlangFile) contextFile.getImportedModulesMap().map { it.key }.toSet() else emptySet()
            val project = context.project
            val parentDirectory = contextFile?.parent
//            val testTargetModule: String = VlangTestFinder.getTestTargetModule(contextFile)
            val module: Module? = if (contextFile != null) ModuleUtilCore.findModuleForPsiElement(contextFile) else null
//            val vendoringEnabled: Boolean = VlangVendoringUtil.isVendoringEnabled(module)
//            val scope: GlobalSearchScope = VlangUtil.VlangPathResolveScope(context)
//            val modules: Collection<VlangFile> =
//                StubIndex.getElements(VlangModulesIndex.KEY, moduleName, project, scope, VlangFile::class.java)

            val modules = VlangModulesIndex.getAll(project)

            return modules.mapNotNull { file ->
//                if (parentDirectory != null && parentDirectory.isEquivalentTo(file.parent)) {
//                    if (testTargetModule != file.getModuleName()) {
//                        return@mapNotNull null
//                    }
//                }
                if (!VlangPsiImplUtil.canBeAutoImported(file, false, module)) {
                    return@mapNotNull null
                }
                val importPath = file.getModuleQualifiedName() // file.getImportPath(vendoringEnabled)
                val parts = importPath.split('.')
                val shortName = parts.last()
                if (shortName != moduleName) {
                    return@mapNotNull null
                }

                if (!imported.contains(importPath))
                    importPath
                else
                    null
            }.toSet().toList()
        }
    }
}
