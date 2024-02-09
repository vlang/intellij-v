package org.vlang.ide.refactoring.rename

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.command.impl.StartMarkAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Pass
import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import org.jetbrains.annotations.TestOnly
import org.vlang.lang.psi.VlangReceiver
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer

class VlangRenameReceiverProcessor : RenamePsiElementProcessor() {
    private var renameOnlyCurrentReceiver = false

    override fun canProcessElement(element: PsiElement) = element is VlangReceiver

    override fun prepareRenaming(element: PsiElement, newName: String, allRenames: MutableMap<PsiElement, String>) {
        if (ApplicationManager.getApplication().isUnitTestMode) {
            renameOnlyCurrentReceiver = testingRenameMode == RENAME_ONLY_CURRENT_RECEIVER
        }
        if (renameOnlyCurrentReceiver) return
        val receiver = element as? VlangReceiver ?: return
        val type = receiver.getType(null)?.unwrapPointer()

        if (type == null) {
            if (LOG.isDebugEnabled) {
                LOG.debug("Cannot rename receiver: type is not found", element.text, element.javaClass.simpleName)
            }
            return
        }

        val project = runReadAction { element.project }
        ProgressManager.getInstance().runProcessWithProgressSynchronously(
            {
                DumbService.getInstance(project).runReadActionInSmartMode {
                    type.ownMethodsList(project)
                        .map { it.receiver }
                        .filter { it.name != "_" }
                        .forEach { allRenames[it] = newName }
                }
            }, "Looking for Method Usages", true, project
        )
    }

    override fun substituteElementToRename(element: PsiElement, editor: Editor, renameCallback: Pass<in PsiElement>) {
        val receiver = element as? VlangReceiver ?: return
        val type = receiver.getType(null)?.unwrapPointer() ?: return
        val methods = type.ownMethodsList(element.project)

        if (methods.size == 1) {
            renameOnlyCurrentReceiver = true
            renameCallback.pass(element)
            return
        }

        if (ApplicationManager.getApplication().isUnitTestMode) {
            LOG.debug("Rename methods of ${type}; rename mode: $testingRenameMode")
            renameOnlyCurrentReceiver = RENAME_ONLY_CURRENT_RECEIVER == testingRenameMode
            renameCallback.pass(element)
            return
        }

        if (StartMarkAction.canStart(editor) != null) {
            renameCallback.pass(element)
            return
        }
        JBPopupFactory.getInstance().createPopupChooserBuilder(listOf(RENAME_ALL_RECEIVERS, RENAME_ONLY_CURRENT_RECEIVER))
            .setMovable(false)
            .setResizable(false)
            .setRequestFocus(true)
            .setItemChosenCallback {
                renameOnlyCurrentReceiver = RENAME_ONLY_CURRENT_RECEIVER == it
                renameCallback.pass(element)
            }
            .createPopup()
            .showInBestPositionFor(editor)
    }

    companion object {
        private val LOG = logger<VlangRenameReceiverProcessor>()
        private var testingRenameMode: String? = null

        @TestOnly
        fun setTestingRenameMode(mode: String, disposable: Disposable) {
            testingRenameMode = mode
            Disposer.register(disposable) {
                EP_NAME.findExtension(VlangRenameReceiverProcessor::class.java)!!.renameOnlyCurrentReceiver = false
                testingRenameMode = null
            }
        }

        fun renameAllReceivers() {
            EP_NAME.findExtension(VlangRenameReceiverProcessor::class.java)?.renameOnlyCurrentReceiver = false
        }

        const val RENAME_ONLY_CURRENT_RECEIVER = "Rename only the current receiver"
        const val RENAME_ALL_RECEIVERS = "Rename all receivers"
    }
}
