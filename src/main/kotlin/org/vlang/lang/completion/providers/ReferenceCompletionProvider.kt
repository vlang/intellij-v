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
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangCompletionUtil.toVlangLookupElement
import org.vlang.lang.completion.VlangLookupElementProperties
import org.vlang.lang.completion.VlangStructLiteralCompletion
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.*
import org.vlang.lang.psi.impl.VlangReference.Companion.isLocalResolve
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.LOCAL_RESOLVE
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.PROCESS_PRIVATE_MEMBERS
import org.vlang.lang.psi.types.VlangArrayTypeEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.isNullableEqual
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangTypeEx

object ReferenceCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val element = parameters.position
        val set = VlangCompletionUtil.withCamelHumpPrefixMatcher(result)

        if (VlangCompletionUtil.isCompileTimeIdentifier(element)) {
            fillCompileTimeConstantsVariants(set)
            return
        }

        val file = element.containingFile as? VlangFile ?: return
        val expression = element.parentOfType<VlangReferenceExpressionBase>() ?: return
        val ref = expression.reference
        if (ref is VlangReference) {
            val refExpression = ref.element as? VlangReferenceExpression
            val variants = VlangStructLiteralCompletion.allowedVariants(refExpression, element)

            fillStructFieldNameVariants(parameters, set, variants, refExpression)

            if (variants != VlangStructLiteralCompletion.Variants.FIELD_NAME_ONLY) {
                ref.processResolveVariants(MyScopeProcessor(parameters, set, file, ref.forTypes))
            }
        } else if (ref is VlangCachedReference<*>) {
            ref.processResolveVariants(MyScopeProcessor(parameters, set, file, false))
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
        parameters: CompletionParameters,
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

        VlangFieldNameReference(refExpression).processResolveVariants(object : MyScopeProcessor(parameters, result, file, false) {
            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                val structFieldName =
                    when (element) {
                        is VlangFieldDefinition    -> element.name
                        is VlangEmbeddedDefinition -> element.type.toEx().name()
                        else                       -> null
                    }

                val structFieldType =
                    when (element) {
                        is VlangFieldDefinition    -> element.getType(null)
                        is VlangEmbeddedDefinition -> element.type.toEx()
                        else                       -> null
                    }

                val structFieldElement = if (element is VlangEmbeddedDefinition) {
                    (element.type.resolveType() as? VlangStructType)?.parent ?: element
                } else {
                    element
                }

                val containingFile = element.containingFile as? VlangFile ?: return true

                // don't add private fields from other modules
                if (element is VlangFieldDefinition && !element.isPublic() && !isLocalResolve(file, containingFile)) {
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
                    .withPresentableText("Fill all fields…")
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
            val element = context.file.findElementAt(offset) ?: return
            val prevElement = element.prevSibling

            val before = if (prevElement.elementType == VlangTypes.LBRACE) "\n" else ""
            val after = if (element.elementType == VlangTypes.RBRACE) "\n" else ""

            val templateText = fields.joinToString("\n", before, after) {
                // we need replace @ to at_ because @ is not allowed in template variable name
                it.first + ": \$field_${it.first.replace("@", "at_")}$"
            }

            val template = TemplateManager.getInstance(project)
                .createTemplate("closures", "vlang", templateText)
            template.isToReformat = true

            fields.forEach {
                template.addVariable("field_${it.first.replace("@", "at_")}", ConstantNode(VlangLangUtil.getDefaultValue(element, it.second)), true)
            }

            TemplateManager.getInstance(project).startTemplate(context.editor, template)
        }
    }

    open class MyScopeProcessor(
        private val parameters: CompletionParameters,
        private val result: CompletionResultSet,
        private val file: VlangFile,
        private val forTypes: Boolean,
    ) : VlangScopeProcessor() {

        private val processedNames = mutableSetOf<String>()

        override fun execute(element: PsiElement, state: ResolveState): Boolean {
            if (accept(element, state, file, forTypes)) {
                addElement(
                    element,
                    state,
                    forTypes,
                    processedNames,
                    result,
                    parameters,
                )
            }
            return true
        }

        private fun accept(e: PsiElement, state: ResolveState, file: VlangFile, forTypes: Boolean): Boolean {
            if (forTypes) {
                if (e !is VlangNamedElement) return false
                if (!e.isPublic() && !state.get(LOCAL_RESOLVE) && !state.get(PROCESS_PRIVATE_MEMBERS)) {
                    return false
                }

                if (e.isBlank()) {
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

            if (e is VlangGlobalVariableDefinition) {
                // global variables are visible everywhere
                return true
            }

            if (e is VlangNamedElement) {
                if (e.isBlank()) {
                    return false
                }

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

                if ((e is VlangMethodDeclaration || e is VlangFieldDefinition) && state.get(PROCESS_PRIVATE_MEMBERS)) {
                    return true
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
        parameters: CompletionParameters,
    ) {
        val lookup = createLookupElement(o, state, forTypes, parameters)
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
        parameters: CompletionParameters,
    ): LookupElement? {
        if (element is VlangFile) {
            return VlangCompletionUtil.createModuleLookupElement(element)
        }

        val elementFile = element.containingFile as? VlangFile ?: return null
        val context = parameters.position
        val contextFile = context.containingFile as? VlangFile ?: return null
        val isSameModule = VlangCodeInsightUtil.sameModule(contextFile, elementFile)

        val contextFunction = context.parentOfType<VlangFunctionOrMethodDeclaration>()
        val elementFunction = element.parentOfType<VlangFunctionOrMethodDeclaration>()
        val isLocal = contextFunction == elementFunction

        val kind = when (element) {
            is VlangFunctionDeclaration       -> VlangLookupElementProperties.ElementKind.FUNCTION
            is VlangMethodDeclaration         -> VlangLookupElementProperties.ElementKind.METHOD
            is VlangStructDeclaration         -> VlangLookupElementProperties.ElementKind.STRUCT
            is VlangEnumDeclaration           -> VlangLookupElementProperties.ElementKind.ENUM
            is VlangInterfaceDeclaration      -> VlangLookupElementProperties.ElementKind.INTERFACE
            is VlangConstDefinition           -> VlangLookupElementProperties.ElementKind.CONSTANT
            is VlangTypeAliasDeclaration      -> VlangLookupElementProperties.ElementKind.TYPE_ALIAS
            is VlangFieldDefinition           -> VlangLookupElementProperties.ElementKind.FIELD
            is VlangInterfaceMethodDefinition -> VlangLookupElementProperties.ElementKind.INTERFACE_METHOD
            is VlangEnumFieldDefinition       -> VlangLookupElementProperties.ElementKind.ENUM_FIELD
            is VlangImportAlias               -> VlangLookupElementProperties.ElementKind.IMPORT_ALIAS
            is VlangGlobalVariableDefinition  -> VlangLookupElementProperties.ElementKind.GLOBAL
            is VlangNamedElement              -> VlangLookupElementProperties.ElementKind.OTHER
            else                              -> return null
        }

        val isTypeCompatible = false
        // TODO: Needs stubs enhancement
        // if (element is VlangTypeOwner) {
        //     val type = element.getType(null)?.unwrapFunction()
        //     val contextType = VlangTypeInferenceUtil.getContextType(context.parent)
        //     isTypeCompatible =
        //         type != null && contextType != null && type !is VlangVoidPtrTypeEx && contextType.isAssignableFrom(type, context.project)
        // }

        var isReceiverTypeCompatible = false
        if (element is VlangMethodDeclaration) {
            val parent = context.parent as? VlangReferenceExpression
            val qualifier = parent?.getQualifier() as? VlangReferenceExpression
            if (qualifier != null) {
                val mutabilityOwner = qualifier.resolve() as? VlangMutabilityOwner

                val mutableMethod = element.isMutable
                val mutableCaller = mutabilityOwner != null && mutabilityOwner.isMutable()
                val isReceiverTypeNotCompatible = mutableMethod && !mutableCaller
                isReceiverTypeCompatible = !isReceiverTypeNotCompatible
            }
        }

        var isContextMember = false
        if (contextFunction is VlangMethodDeclaration) {
            if (element is VlangFieldDefinition || element is VlangMethodDeclaration) {
                element as VlangNamedElement
                val ownerType = (element.getOwner() as? VlangNamedElement)?.getType(null)
                val receiverType = contextFunction.receiverType.toEx().unwrapPointer()
                if (ownerType.isNullableEqual(receiverType)) {
                    isContextMember = true
                }
            }
        }

        val lookupElement = when (element) {
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
            is VlangGlobalVariableDefinition  -> VlangCompletionUtil.createGlobalVariableLikeLookupElement(element, state)
            is VlangImportAlias               -> VlangCompletionUtil.createImportAliasLookupElement(element)
            is VlangNamedElement              -> VlangCompletionUtil.createVariableLikeLookupElement(element)
            else                              -> null
        }

        var isContextElement = false
        if (lookupElement is PrioritizedLookupElement<*>) {
            isContextElement = lookupElement.priority.toInt() == VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY
        }

        val isNotDeprecated = element !is VlangNamedElement || !element.isDeprecated()

        return lookupElement?.toVlangLookupElement(
            VlangLookupElementProperties(
                isLocal = isLocal,
                isSameModule = isSameModule,
                elementKind = kind,
                isReceiverTypeCompatible = isReceiverTypeCompatible,
                isTypeCompatible = isTypeCompatible,
                isContextElement = isContextElement,
                isNotDeprecated = isNotDeprecated,
                isContextMember = isContextMember,
            )
        )
    }
}
