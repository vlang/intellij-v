package org.vlang.ide.refactoring

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.ide.util.gotoByName.ChooseByNameItemProvider
import com.intellij.ide.util.gotoByName.ChooseByNamePopup
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider
import com.intellij.lang.LanguageCodeInsightActionHandler
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.testFramework.TestModeFlags
import com.intellij.util.FilteringProcessor
import com.intellij.util.Processor
import com.intellij.util.Processors
import com.intellij.util.indexing.FindSymbolParameters
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.navigation.goto.VlangGotoClassLikeContributor
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangFunctionTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.lang.search.VlangGotoUtil
import org.vlang.lang.search.VlangSuperSearch

class VlangImplementMethodsHandler : LanguageCodeInsightActionHandler {
    override fun isValidFor(editor: Editor, file: PsiFile) = file is VlangFile

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        if (file !is VlangFile) return

        val element = file.findElementAt(editor.caretModel.offset) ?: return
        val struct = findStructDeclaration(element) ?: return

        if (ApplicationManager.getApplication().isUnitTestMode) {
            val testingInterfaceToImplement = TestModeFlags.get(TESTING_INTERFACE_TO_IMPLEMENT)
            if (testingInterfaceToImplement != null) {
                startImplementing(project, file, editor, struct, testingInterfaceToImplement)
                return
            }
        }

        val oldPopup = project.getUserData(ChooseByNamePopup.CHOOSE_BY_NAME_POPUP_IN_PROJECT_KEY)
        oldPopup?.close(false)

        val model =
            VlangTypeContributorsBasedGotoByModel(project, MyGotoClassLikeContributor(file, struct), "Choose interface to implement")
        val provider = DefaultChooseByNameItemProvider(file)
        val popup = MyChooseByNamePopup(project, model, provider, oldPopup)

        project.putUserData(ChooseByNamePopup.CHOOSE_BY_NAME_POPUP_IN_PROJECT_KEY, popup)
        popup.isSearchInAnyPlace = true

        popup.invoke(object : ChooseByNamePopupComponent.Callback() {
            override fun elementChosen(element: Any) {
                // do not edit anything here. Popup is not closed yet and focus is nowhere,
                // it may break UndoRedo mechanism (see FocusBasedCurrentEditorProvider)
            }

            override fun onClose() {
                Disposer.dispose(model)
                if (!popup.closedCorrectly) {
                    return
                }
                val chosenElement = popup.chosenElement!!
                if (chosenElement is VlangInterfaceDeclaration && !project.isDisposed) {
                    IdeFocusManager.getInstance(project).doWhenFocusSettlesDown {
                        startImplementing(project, file, editor, struct, chosenElement)
                    }
                }
            }
        }, ModalityState.current(), true)
    }

    private fun startImplementing(
        project: Project,
        file: VlangFile,
        editor: Editor,
        struct: VlangStructDeclaration,
        iface: VlangInterfaceDeclaration,
    ) {
        val addedFields = mutableListOf<VlangTypeEx>()

        WriteCommandAction.runWriteCommandAction(project) {
            val fields = iface.interfaceType.fieldList
            for (field in fields) {
                if (field.name == null || isAlreadyImplementedField(struct, field)) continue

                val type = field.getType(null)
                val typeName = type?.readableName(struct) ?: continue
                val name = field.name ?: continue

                struct.addField(name, typeName, field.isMutable())
                addedFields.add(type)
            }
        }

        val addedSignatures = mutableListOf<VlangSignature>()
        val methods = iface.interfaceType.methodList

        val templateText = buildString {
            var firstMethod = true

            for (method in methods) {
                val signature = method.getSignature()
                if (method == null || method.name == null || isAlreadyImplementedMethod(struct, method)) continue

                val mutable = if (method.isMutable) "mut " else ""
                val reference = if (method.isMutable) "" else "&"
                val end = if (firstMethod) "\$END$" else ""

                if (firstMethod) {
                    appendLine()
                    firstMethod = false
                }

                append(
                    """
    fn ($mutable${"$"}RECEIVER$ $reference${struct.name}) ${method.name}${processSignature(signature, struct)} {
        panic('not implemented')$end
    }
    """
                )

                addedSignatures.add(signature)
            }
        }

        WriteCommandAction.runWriteCommandAction(project) {
            for (addedSignature in addedSignatures) {
                VlangLangUtil.importTypesFromSignature(addedSignature, file)
            }

            for (addedField in addedFields) {
                VlangLangUtil.importType(addedField, file)
            }
        }

        val template = TemplateManager.getInstance(project)
            .createTemplate("struct", "Vlang", templateText.trimEnd())

        if (templateText.isNotEmpty()) {
            val receiverNames = generateReceiverNames(struct)
            template.addVariable(
                "RECEIVER", ConstantNode(receiverNames.first())
                    .withLookupItems(
                        receiverNames.map { LookupElementBuilder.create(it) }
                    ), true
            )
        }

        editor.caretModel.moveToOffset(struct.endOffset)

        template.isToReformat = true
        TemplateManager.getInstance(project).startTemplate(editor, template)
    }

    private fun processSignature(signature: VlangSignature, context: PsiElement): String {
        return buildString {
            append("(")
            val parameters = signature.parameters.paramDefinitionList

            append(
                parameters.joinToString(", ") { parameter ->
                    buildString {
                        append(parameter.name)
                        append(" ")
                        append(processType(parameter.type.toEx(), context))
                    }
                }
            )

            append(")")

            if (signature.result != null) {
                append(" ")
                append(processType(signature.result!!.type.toEx(), context))
            }
        }
    }

    private fun processType(type: VlangTypeEx, context: PsiElement): String {
        return type.readableName(context)
    }

    private fun isAlreadyImplementedMethod(struct: VlangStructDeclaration, method: VlangInterfaceMethodDefinition): Boolean {
        val structMethods = struct.structType.toEx().methodsList(struct.project)
        return structMethods
            .filter { it.name == method.name }
            // if interface method immutable, don't care about struct method mutability
            .filter { !method.isMutable || it.receiver.isMutable() == method.isMutable }
            .any { structMethod ->
                val interfaceTypeEx = VlangFunctionTypeEx.from(method) ?: return@any false
                val structTypeEx = VlangFunctionTypeEx.from(structMethod) ?: return@any false

                interfaceTypeEx.isEqual(structTypeEx)
            }
    }

    private fun isAlreadyImplementedField(struct: VlangStructDeclaration, field: VlangFieldDefinition): Boolean {
        val structFields = struct.structType.fieldList
        return structFields
            .filter { it.name == field.name }
            // if interface field immutable, don't care about struct field mutability
            .filter { !field.isMutable() || it.isMutable() == field.isMutable() }
            .any { structField ->
                val structFieldType = structField.getType(null) ?: return@any false
                val interfaceFieldType = field.getType(null) ?: return@any false
                structFieldType.isEqual(interfaceFieldType)
            }
    }

    private fun generateReceiverNames(struct: VlangStructDeclaration): List<String> {
        val firstLetter = struct.name.first().lowercaseChar().toString()
        val other = listOf(firstLetter, "this")

        val structMethods = struct.structType.toEx().methodsList(struct.project)
        val first = VlangLangUtil.getUsedReceiverNameOrDefault(structMethods, null)
        if (first != null) {
            return listOf(first) + other
        }

        return other
    }

    class MyGotoClassLikeContributor(
        private val file: PsiFile,
        private val struct: VlangStructDeclaration,
    ) : VlangGotoClassLikeContributor() {
        private var alreadyImplementedTypes: Set<VlangInterfaceDeclaration>? = null

        private fun getAlreadyImplementedTypes(): Set<VlangInterfaceDeclaration> {
            if (alreadyImplementedTypes == null) {
                val set = mutableSetOf<VlangInterfaceDeclaration>()
                VlangSuperSearch.execute(
                    VlangGotoUtil.param(struct),
                    Processors.cancelableCollectProcessor(set as Collection<VlangNamedElement>)
                )
                alreadyImplementedTypes = set
            }
            return alreadyImplementedTypes!!
        }

        override fun processElementsWithName(
            name: String,
            processor: Processor<in NavigationItem>,
            parameters: FindSymbolParameters,
        ) {
            val alreadyImplementedTypes = getAlreadyImplementedTypes()
            super.processElementsWithName(name, FilteringProcessor(Condition {
                if (it !is VlangInterfaceDeclaration) return@Condition false
                if (alreadyImplementedTypes.contains(it)) return@Condition false
                val fromFile = it.containingFile ?: return@Condition false
                it.isPublic() || VlangCodeInsightUtil.isDirectlyAccessible(fromFile, file)
            }, processor), parameters)
        }
    }

    class MyChooseByNamePopup(
        project: Project,
        model: VlangTypeContributorsBasedGotoByModel,
        provider: ChooseByNameItemProvider,
        oldPopup: ChooseByNamePopup?,
    ) : ChooseByNamePopup(project, model, provider, oldPopup, null, false, 0) {

        var closedCorrectly = true

        override fun close(isOk: Boolean) {
            if (!checkDisposed()) {
                closedCorrectly = isOk
            }
            super.close(isOk)
        }
    }

    companion object {
        val TESTING_INTERFACE_TO_IMPLEMENT: Key<VlangInterfaceDeclaration> =
            Key.create("VlangImplementMethodsHandler.TESTING_INTERFACE_TO_IMPLEMENT")

        fun findStructDeclaration(element: PsiElement) = element.parentOfType<VlangStructDeclaration>()
    }
}
