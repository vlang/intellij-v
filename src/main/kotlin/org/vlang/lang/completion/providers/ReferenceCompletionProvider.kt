package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangStructLiteralCompletion
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.*
import org.vlang.lang.psi.impl.VlangReference.Companion.isLocalResolve
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.LOCAL_RESOLVE
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object ReferenceCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val element = parameters.position

        if (VlangCompletionUtil.isCompileTimeIdentifier(element)) {
            fillCompileTimeConstantsVariants(result)
            return
        }

        val file = element.containingFile as? VlangFile ?: return
        val expression = element.parentOfType<VlangReferenceExpressionBase>() ?: return
        val ref = expression.reference
        if (ref is VlangReference) {
            val refExpression = ref.element as? VlangReferenceExpression
            val variants = VlangStructLiteralCompletion.allowedVariants(refExpression, element)

            fillStructFieldNameVariants(result, variants, refExpression)

            if (variants != VlangStructLiteralCompletion.Variants.FIELD_NAME_ONLY) {
                ref.processResolveVariants(MyScopeProcessor(result, file, ref.forTypes))
            }
        } else if (ref is VlangCachedReference<*>) {
            ref.processResolveVariants(MyScopeProcessor(result, file, false))
        }
    }

    private fun fillCompileTimeConstantsVariants(result: CompletionResultSet) {
        result.addAllElements(
            VlangCompletionUtil.compileTimeConstants.map {
                VlangCompletionUtil.createCompilePseudoVarLookupElement(it.key, it.value)
            }
        )
    }

    private fun fillStructFieldNameVariants(
        result: CompletionResultSet,
        variants: VlangStructLiteralCompletion.Variants,
        refExpression: VlangReferenceExpression?,
    ) {
        if (refExpression == null ||
            variants !== VlangStructLiteralCompletion.Variants.FIELD_NAME_ONLY &&
            variants !== VlangStructLiteralCompletion.Variants.BOTH
        ) {
            return
        }

        val possiblyLiteralValueExpression = refExpression.parentOfType<VlangLiteralValueExpression>()
        val file = refExpression.containingFile as? VlangFile ?: return
        val fields = mutableSetOf<Pair<String, VlangTypeEx?>>()
        val elementList =
            possiblyLiteralValueExpression?.elementList
                ?: refExpression.parentOfType<VlangArgumentList>()?.elementList
                ?: emptyList()

        val alreadyAssignedFields = VlangStructLiteralCompletion.alreadyAssignedFields(elementList)

        VlangFieldNameReference(refExpression).processResolveVariants(object : MyScopeProcessor(result, file, false) {
            override fun execute(o: PsiElement, state: ResolveState): Boolean {
                val structFieldName =
                    when (o) {
                        is VlangFieldDefinition    -> o.name
                        is VlangEmbeddedDefinition -> o.type.toEx().name()
                        else                       -> null
                    }

                val structFieldType =
                    when (o) {
                        is VlangFieldDefinition    -> o.getType(null)
                        is VlangEmbeddedDefinition -> o.type.toEx()
                        else                       -> null
                    }

                val structFieldElement = if (o is VlangEmbeddedDefinition) {
                    (o.type.resolveType() as? VlangStructType)?.parent ?: o
                } else {
                    o
                }

                val containingFile = o.containingFile as? VlangFile ?: return true

                // don't add private fields from other modules
                if (o is VlangFieldDefinition && !o.isPublic() && !isLocalResolve(file, containingFile)) {
                    return true
                }

                if (structFieldName != null) {
                    fields.add(structFieldName to structFieldType)
                }

                // При инициализации структуры мы можем использовать приватные поля
                val newState = state.put(LOCAL_RESOLVE, true)

                if (structFieldName != null && alreadyAssignedFields.contains(structFieldName)) {
                    return true
                }

                return super.execute(structFieldElement, newState)
            }
        })

        if (possiblyLiteralValueExpression != null) {
            val type = possiblyLiteralValueExpression.getType(null)
            if (type is VlangArrayTypeEx) {
                // don't add "Fill all field…" for array init
                return
            }

            val remainingFields = fields.filter { !alreadyAssignedFields.contains(it.first) }
            if (remainingFields.size > 1) {
                val element = LookupElementBuilder.create("")
                    .withPresentableText("Fill all field…")
                    .withIcon(AllIcons.Actions.RealIntentionBulb)
                    .withInsertHandler(StructFieldsInsertHandler(remainingFields))

                result.addElement(element)
            }
        }
    }

    class StructFieldsInsertHandler(val fields: List<Pair<String, VlangTypeEx?>>) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val project = context.project
            val offset = context.editor.caretModel.offset
            val element = context.file.findElementAt(offset)
            val prevElement = element?.prevSibling

            val before = if (prevElement.elementType == VlangTypes.LBRACE) "\n" else ""
            val after = if (element.elementType == VlangTypes.RBRACE) "\n" else ""

            val templateText = fields.joinToString("\n", before, after) {
                it.first + ": \$field_${it.first}$"
            }

            val template = TemplateManager.getInstance(project)
                .createTemplate("closures", "vlang", templateText)
            template.isToReformat = true

            fields.forEach {
                template.addVariable("field_${it.first}", ConstantNode(VlangLangUtil.getDefaultValue(it.second)), true)
            }

            TemplateManager.getInstance(project).startTemplate(context.editor, template)
        }
    }

    private open class MyScopeProcessor(
        private val result: CompletionResultSet,
        private val file: VlangFile,
        private val forTypes: Boolean,
    ) : VlangScopeProcessor() {

        private val processedNames = mutableSetOf<String>()

        override fun execute(o: PsiElement, state: ResolveState): Boolean {
            if (accept(o, state, file, forTypes)) {
                addElement(
                    o,
                    state,
                    forTypes,
                    processedNames,
                    result
                )
            }
            return true
        }

        private fun accept(e: PsiElement, state: ResolveState, file: VlangFile, forTypes: Boolean): Boolean {
            if (forTypes) {
                if (e !is VlangNamedElement) return false
                if (!e.isPublic() && !state.get(LOCAL_RESOLVE)) {
                    return false
                }

                // forbid raw map completion
                if (e is VlangStructDeclaration && e.name == "map") {
                    return false
                }

                return e is VlangStructDeclaration ||
                        e is VlangEnumDeclaration ||
                        e is VlangTypeAliasDeclaration ||
                        e is VlangInterfaceDeclaration
            }

            if (e is VlangFile) {
                return true
            }

            if (e is VlangImportAlias) {
                return true
            }

            if (e is VlangNamedElement) {
                if (e.name?.startsWith("C.") == true) {
                    return file.isCFile()
                }

                if (e.name?.startsWith("JS.") == true) {
                    return file.isJSFile()
                }

                // forbid raw map completion
                if (e is VlangStructDeclaration && e.name == "map") {
                    return false
                }

                return state.get(LOCAL_RESOLVE) || e.isPublic()
            }

            return false
        }

        override fun isCompletion(): Boolean = true
    }

    private fun addElement(
        o: PsiElement,
        state: ResolveState,
        forTypes: Boolean,
        processedNames: MutableSet<String>,
        set: CompletionResultSet,
    ) {
        val lookup = createLookupElement(o, state, forTypes)
        if (lookup != null) {
            val key = lookup.lookupString + o.javaClass
            if (!processedNames.contains(key)) {
                set.addElement(lookup)
                processedNames.add(key)
            }
        }
    }

    private fun createLookupElement(
        element: PsiElement,
        state: ResolveState,
        forTypes: Boolean,
    ): LookupElement? {
        if (element is VlangFile) {
            return VlangCompletionUtil.createModuleLookupElement(element)
        }

        return when (element) {
            is VlangFunctionDeclaration       -> VlangCompletionUtil.createFunctionLookupElement(element, state)
            is VlangMethodDeclaration         -> VlangCompletionUtil.createMethodLookupElement(element)
            is VlangStructDeclaration         -> VlangCompletionUtil.createStructLookupElement(element, state, !forTypes)
            is VlangEnumDeclaration           -> VlangCompletionUtil.createEnumLookupElement(element, state)
            is VlangInterfaceDeclaration      -> VlangCompletionUtil.createInterfaceLookupElement(element, state)
            is VlangTypeAliasDeclaration      -> VlangCompletionUtil.createTypeAliasLookupElement(element, state)
            is VlangFieldDefinition           -> VlangCompletionUtil.createFieldLookupElement(element)
            is VlangInterfaceMethodDefinition -> VlangCompletionUtil.createInterfaceMethodLookupElement(element, state)
            is VlangConstDefinition           -> VlangCompletionUtil.createConstantLookupElement(element, state)
            is VlangEnumFieldDefinition       -> VlangCompletionUtil.createEnumFieldLookupElement(element)
            is VlangGlobalVariableDefinition  -> VlangCompletionUtil.createGlobalVariableLikeLookupElement(element)
            is VlangImportAlias               -> VlangCompletionUtil.createImportAliasLookupElement(element)
            is VlangNamedElement              -> VlangCompletionUtil.createVariableLikeLookupElement(element)
            else                              -> null
        }
    }
}
