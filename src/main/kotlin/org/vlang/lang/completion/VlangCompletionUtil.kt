package org.vlang.lang.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.lookup.LookupElementRenderer
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.MODULE_NAME
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.NEED_QUALIFIER_NAME
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangFunctionTypeEx
import javax.swing.Icon

object VlangCompletionUtil {
    const val KEYWORD_PRIORITY = 0
    const val CONTEXT_COMPLETION_PRIORITY = 20
    const val CONTEXT_KEYWORD_PRIORITY = 0
    const val NOT_IMPORTED_METHOD_PRIORITY = 0
    const val METHOD_PRIORITY = NOT_IMPORTED_METHOD_PRIORITY + 0
    const val NOT_IMPORTED_FUNCTION_PRIORITY = 0
    const val FUNCTION_PRIORITY = NOT_IMPORTED_FUNCTION_PRIORITY + 0
    const val NOT_IMPORTED_STRUCT_PRIORITY = 0
    const val STRUCT_PRIORITY = NOT_IMPORTED_STRUCT_PRIORITY + 0
    const val NOT_IMPORTED_TYPE_ALIAS_PRIORITY = 0
    const val TYPE_ALIAS_PRIORITY = NOT_IMPORTED_TYPE_ALIAS_PRIORITY + 0
    const val NOT_IMPORTED_CONSTANT_PRIORITY = 0
    const val CONSTANT_PRIORITY = NOT_IMPORTED_CONSTANT_PRIORITY + 0
    const val COMPILE_TIME_CONSTANT_PRIORITY = CONSTANT_PRIORITY + 0
    const val NOT_IMPORTED_TYPE_PRIORITY = 0
    const val TYPE_PRIORITY = NOT_IMPORTED_TYPE_PRIORITY + 0
    const val NOT_IMPORTED_TYPE_CONVERSION = 0
    const val TYPE_CONVERSION = NOT_IMPORTED_TYPE_CONVERSION + 0
    const val GLOBAL_VAR_PRIORITY = 0
    const val NOT_IMPORTED_VAR_PRIORITY = 0
    const val VAR_PRIORITY = NOT_IMPORTED_VAR_PRIORITY + 0
    const val FIELD_PRIORITY = 16
    const val LABEL_PRIORITY = 0
    const val MODULE_PRIORITY = 0

    val compileTimeConstants = mapOf(
        "FN" to "The name of the current function",
        "METHOD" to "The name of the current method",
        "MOD" to "The name of the current module",
        "STRUCT" to "The name of the current struct",
        "FILE" to "The absolute path to the current file",
        "LINE" to "The line number of the current line (as a string)",
        "FILE_LINE" to "The relative path and line number of the current line (like @FILE:@LINE)",
        "COLUMN" to "The column number of the current line (as a string)",
        "VEXE" to "The absolute path to the V compiler executable",
        "VEXEROOT" to "The absolute path to the V compiler executable's root directory",
        "VHASH" to "The V compiler's git hash",
        "VMOD_FILE" to "The content to the nearest v.mod file",
        "VMODROOT" to "The absolute path to the nearest v.mod file's directory",
    )

    fun withCamelHumpPrefixMatcher(resultSet: CompletionResultSet): CompletionResultSet {
        return resultSet.withPrefixMatcher(createPrefixMatcher(resultSet.prefixMatcher.prefix))
    }

    fun createPrefixMatcher(prefix: String): PrefixMatcher {
        return CamelHumpMatcher(prefix, false)
    }

    fun LookupElementBuilder.withPriority(priority: Int): LookupElement {
        return PrioritizedLookupElement.withPriority(this, priority.toDouble())
    }

    fun LookupElement.toVlangLookupElement(properties: VlangLookupElementProperties): LookupElement {
        return VlangLookupElement(this, properties)
    }

    fun isCompileTimeIdentifier(element: PsiElement): Boolean {
        return element.elementType == VlangTypes.IDENTIFIER && element.text.startsWith("@")
    }

    fun isCompileTimeMethodIdentifier(element: PsiElement): Boolean {
        return element.elementType == VlangTypes.IDENTIFIER && element.text.startsWith("$")
    }

    fun shouldSuppressCompletion(element: PsiElement): Boolean {
        val parent = element.parent
        val grand = parent.parent
        if (grand is VlangVarDeclaration && grand.varDefinitionList.any { PsiTreeUtil.isAncestor(it, element, false) }) {
            return true
        }

        if (parent is VlangFunctionDeclaration || parent is VlangMethodName) {
            return true
        }

        return parent is VlangStructType || parent is VlangEnumDeclaration || parent is VlangInterfaceType
    }

    fun createModuleLookupElement(element: VlangFile): LookupElement? {
        val name = element.getModuleName()
        if (name.isNullOrEmpty()) {
            return null
        }
        val fqn = element.getModuleQualifiedName()
        return createModuleLookupElement(
            element, name,
            insertHandler = ModuleInsertHandler(fqn),
            priority = MODULE_PRIORITY
        )
    }

    fun createImportAliasLookupElement(element: VlangImportAlias): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }
        return createImportAliasLookupElement(
            element, name,
            priority = MODULE_PRIORITY
        )
    }

    fun createVariableLikeLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createVariableLikeLookupElement(
            element, name,
            priority = VAR_PRIORITY
        )
    }

    fun createGlobalVariableLikeLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        val moduleName = state.get(MODULE_NAME)
        return createGlobalVariableLookupElement(
            element, name,
            insertHandler = GlobalVariableInsertHandler(moduleName),
            priority = GLOBAL_VAR_PRIORITY
        )
    }

    class GlobalVariableInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {}
    }

    fun createFunctionLookupElement(element: VlangFunctionDeclaration, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createFunctionLookupElement(
            element, name, moduleName, state,
            insertHandler = FunctionInsertHandler(element, moduleName),
            priority = FUNCTION_PRIORITY,
        )
    }

    fun createConstantLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createConstantLookupElement(
            element, name, moduleName, state,
            insertHandler = ConstantInsertHandler(moduleName),
            priority = CONSTANT_PRIORITY,
        )
    }

    fun createEnumFieldLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createEnumFieldLookupElement(
            element, name,
            priority = FIELD_PRIORITY,
        )
    }

    fun createMethodLookupElement(element: VlangMethodDeclaration): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createMethodLookupElement(
            element, name,
            insertHandler = FunctionInsertHandler(element, null),
            priority = METHOD_PRIORITY,
        )
    }

    fun createFieldLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        return createFieldLookupElement(
            element, name,
            priority = FIELD_PRIORITY,
            insertHandler = FieldInsertHandler()
        )
    }

    fun createInterfaceMethodLookupElement(element: VlangInterfaceMethodDefinition, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createInterfaceMethodLookupElement(
            element, name, moduleName, state,
            priority = METHOD_PRIORITY,
            insertHandler = FunctionInsertHandler(element, null)
        )
    }

    fun createStructLookupElement(element: VlangStructDeclaration, state: ResolveState, needBrackets: Boolean): LookupElement? {
        val name = element.name
        if (name.isEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createStructLookupElement(
            element, name, moduleName, state,
            priority = STRUCT_PRIORITY,
            insertHandler = StructInsertHandler(moduleName, element.structType, needBrackets)
        )
    }

    fun createCompilePseudoVarLookupElement(name: String, description: String): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.create("@$name")
                .withTypeText(description)
                .withIcon(VIcons.Constant), COMPILE_TIME_CONSTANT_PRIORITY.toDouble()
        )
    }

    fun createEnumLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? =
        createClassLikeLookupElement(element, state, AllIcons.Nodes.Enum, STRUCT_PRIORITY)

    fun createInterfaceLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? =
        createClassLikeLookupElement(element, state, AllIcons.Nodes.Interface, STRUCT_PRIORITY)

    fun createTypeAliasLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createTypeAliasLookupElement(
            element, name, moduleName, state,
            priority = TYPE_ALIAS_PRIORITY,
            insertHandler = ClassLikeInsertHandler(moduleName)
        )
    }

    private fun createClassLikeLookupElement(element: VlangNamedElement, state: ResolveState, icon: Icon, priority: Int): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createClassLikeLookupElement(
            element, name, icon, moduleName, state,
            priority = priority,
        )
    }

    private fun createClassLikeLookupElement(
        element: VlangNamedElement, lookupString: String,
        icon: Icon, moduleName: String?, state: ResolveState,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(ClassLikeRenderer(icon, moduleName))
                .withInsertHandler(ClassLikeInsertHandler(moduleName)), priority.toDouble()
        )
    }

    private fun createTypeAliasLookupElement(
        element: VlangNamedElement, lookupString: String, moduleName: String?, state: ResolveState,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(TypeAliasRenderer(moduleName))
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }


    private fun createStructLookupElement(
        element: VlangStructDeclaration, lookupString: String, moduleName: String?, state: ResolveState,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        val icon = if (element.isUnion) VIcons.Union else VIcons.Struct
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(ClassLikeRenderer(icon, moduleName))
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    fun createFieldLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(FIELD_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createInterfaceMethodLookupElement(
        element: VlangNamedElement, lookupString: String, moduleName: String?, state: ResolveState,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(INTERFACE_METHOD_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createFunctionLookupElement(
        element: VlangNamedElement, lookupString: String, moduleName: String?, state: ResolveState,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(FunctionRenderer(moduleName))
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createQualifiedName(state: ResolveState, lookupString: String): String {
        val needQualifier = state.get(NEED_QUALIFIER_NAME) ?: true
        if (!needQualifier) {
            return lookupString
        }

        val moduleName = state.get(MODULE_NAME)
        val lastPart = moduleName?.substringAfterLast('.') ?: moduleName
        return if (moduleName != null) "$lastPart.$lookupString" else lookupString
    }

    fun createMethodLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(METHOD_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createConstantLookupElement(
        element: VlangNamedElement, lookupString: String, moduleName: String?, state: ResolveState,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(ConstantRenderer(moduleName))
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    fun createEnumFieldLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(ENUM_FIELD_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createModuleLookupElement(
        element: VlangFile, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(MODULE_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createImportAliasLookupElement(
        element: VlangImportAlias, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(IMPORT_ALIAS_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    fun createVariableLikeLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(VARIABLE_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    fun createGlobalVariableLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(VARIABLE_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    fun showCompletion(editor: Editor) {
        AutoPopupController.getInstance(editor.project!!).autoPopupMemberLookup(editor, null)
    }

    abstract class ElementInsertHandler(private val moduleName: String?) : InsertHandler<LookupElement> {
        open fun handleInsertion(context: InsertionContext, item: LookupElement) {}

        final override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset
            val file = context.file as VlangFile
            val element = file.findElementAt(caretOffset - 1) ?: return
            if (element.parentOfType<VlangSelectiveImportList>() != null) {
                return
            }

            context.commitDocument()

            handleInsertion(context, item)

            context.commitDocument()

            if (!moduleName.isNullOrEmpty()) {
                file.addImport(moduleName, null)
            }
        }
    }

    class ModuleInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, ".")
            context.editor.caretModel.moveToOffset(caretOffset + 1)

            showCompletion(context.editor)
        }
    }

    class FunctionInsertHandler(private val function: VlangSignatureOwner, moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset
            val element = context.file.findElementAt(caretOffset - 1)

            if (element != null) {
                val type = VlangTypeInferenceUtil.getContextType(element.parent)?.unwrapPointer()?.unwrapAlias()

                if (type is VlangFunctionTypeEx) {
                    // we don't want to add parentheses for function if context type is function type
                    //
                    // Example:
                    // struct Name {
                    // 	 cb fn ()
                    // }
                    //
                    // fn foo() {}
                    //
                    // fn main() {
                    // 	 n := Name{
                    // 	   cb: foo // don't add parentheses here
                    // 	 }
                    // }
                    return
                }
            }

            val takeZeroArguments = VlangCodeInsightUtil.takeZeroArguments(function)
            val isGeneric = function.genericParameters != null

            val prevChar = context.document.charsSequence.getOrNull(caretOffset)
            val withParenAfterCursor = prevChar == '('
            val withLeftBracketParenAfterCursor = prevChar == '['

            if (!withParenAfterCursor && !isGeneric) {
                try {
                    context.document.insertString(caretOffset, "()")
                } catch (e: Exception) {
                    return
                }
            }
            if (!withLeftBracketParenAfterCursor && isGeneric) {
                val endVariable = if (!takeZeroArguments) "\$END$" else ""
                genericParametersInsertHandler(function.genericParameters!!, "($endVariable)")
                    .handleInsert(context, item)
            }
            if (takeZeroArguments && !isGeneric) {
                // move after ()
                context.editor.caretModel.moveToOffset(caretOffset + 2)
                return
            }
            context.editor.caretModel.moveToOffset(caretOffset + 1)
        }
    }

    private fun genericParametersInsertHandler(params: VlangGenericParameters, suffix: String = ""): TemplateStringInsertHandler {
        val paramList = params.parameters.map {
            it.name!!
        }

        return TemplateStringInsertHandler(
            "[" + paramList.joinToString(", ") { "\$$it$" } + "]" + suffix, true,
            *paramList.map {
                it to ConstantNode(it)
            }.toTypedArray()
        )
    }

    class ConstantInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName)

    class StructInsertHandler(
        moduleName: String?,
        private val struct: VlangStructType,
        private val needBrackets: Boolean = true,
    ) : ElementInsertHandler(moduleName) {

        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            val prevChar = context.document.charsSequence.getOrNull(caretOffset)
            val withLeftBracketParenAfterCursor = prevChar == '['

            val isGeneric = struct.genericParameters != null
            if (isGeneric && !withLeftBracketParenAfterCursor) {
                val suffix = if (needBrackets) "{\$END$}" else ""
                genericParametersInsertHandler(struct.genericParameters!!, suffix)
                    .handleInsert(context, item)
            }

            if (!isGeneric && needBrackets) {
                context.document.insertString(caretOffset, "{}")
                context.editor.caretModel.moveToOffset(caretOffset + 1)

                showCompletion(context.editor)
            }
        }
    }

    class ClassLikeInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {}
    }

    class StringInsertHandler(val string: String, val shift: Int, val enable: Boolean = true) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            if (!enable) return

            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, string)
            context.editor.caretModel.moveToOffset(caretOffset + shift)
        }
    }

    class TemplateStringInsertHandler(
        private val string: String,
        private val reformat: Boolean = true,
        vararg val variables: Pair<String, Expression>,
    ) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val template = TemplateManager.getInstance(context.project)
                .createTemplate("templateInsertHandler", "vlang", string)
            template.isToReformat = reformat

            variables.forEach { (name, expression) ->
                template.addVariable(name, expression, true)
            }

            TemplateManager.getInstance(context.project).startTemplate(context.editor, template)
        }
    }

    class FieldInsertHandler : SingleCharInsertHandler(':', needSpace = true) {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val file = context.file as? VlangFile ?: return
            context.commitDocument()

            val offset = context.startOffset
            val at = file.findElementAt(offset)

            val ref = PsiTreeUtil.getParentOfType(at, VlangValue::class.java, VlangReferenceExpression::class.java)
            if (ref is VlangReferenceExpression && (ref.getQualifier() != null)) {
                return
            }

            val value = PsiTreeUtil.getParentOfType(at, VlangValue::class.java)
            if (value == null || PsiTreeUtil.getPrevSiblingOfType(value, VlangKey::class.java) != null) {
                return
            }
            super.handleInsert(context, item)
        }
    }

    private val MODULE_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangFile ?: return
            val moduleName = elem.getModuleName()
            val qualifier = elem.getModuleQualifiedName().substringBeforeLast('.', "")

            p.icon = VIcons.Directory
            p.tailText = " $qualifier"
            p.isTypeGrayed = true
            p.itemText = moduleName
        }
    }

    private val IMPORT_ALIAS_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangImportAlias ?: return
            val importSpec = elem.parent as? VlangImportSpec ?: return

            p.icon = VIcons.Directory
            p.tailText = " alias for ${importSpec.name}"
            p.isTypeGrayed = true
            p.itemText = element.lookupString
            p.isStrikeout = elem.isDeprecated()
        }
    }

    private val VARIABLE_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangNamedElement ?: return
            val type = elem.getType(null)?.readableName(elem)
            val icon = when (elem) {
                is VlangVarDefinition -> VIcons.Variable
                is VlangGlobalVariableDefinition -> VIcons.Variable
                is VlangParamDefinition -> VIcons.Parameter
                is VlangReceiver -> VIcons.Receiver
                is VlangEmbeddedDefinition -> VIcons.Field
                else -> null
            }

            val additionalText = when (elem) {
                is VlangGlobalVariableDefinition -> {
                    val file = elem.containingFile as? VlangFile
                    val moduleName = file?.getModuleName() ?: "unknown"
                    " (global defined in $moduleName)"
                }
                else -> ""
            }

            p.icon = icon
            p.typeText = type
            p.isTypeGrayed = true
            p.tailText = additionalText
            p.itemText = element.lookupString
            p.isStrikeout = elem.isDeprecated()
        }
    }

    private val METHOD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangMethodDeclaration ?: return
            val signature = elem.getSignature()

            val genericParameters = elem.genericParameters?.text ?: ""
            val typeText = signature?.result?.text ?: ""
            val icon = VIcons.Method

            val type = elem.receiverType.toEx().readableName(elem)
            p.tailText = signature?.parameters?.text + " of " + type

            p.icon = icon
            p.typeText = typeText
            p.isTypeGrayed = true
            p.itemText = element.lookupString + genericParameters
            p.isStrikeout = elem.isDeprecated()
        }
    }

    private val INTERFACE_METHOD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangInterfaceMethodDefinition ?: return
            val signature = elem.getSignature()

            val genericParameters = elem.genericParameters?.text ?: ""
            val typeText = signature.result?.text ?: ""
            val icon = VIcons.Method

            val parent = elem.parentOfType<VlangNamedElement>()
            p.tailText = signature.parameters.text + " of " + parent?.name

            p.icon = icon
            p.typeText = typeText
            p.isTypeGrayed = true
            p.itemText = element.lookupString + genericParameters
            p.isStrikeout = elem.isDeprecated()
        }
    }

    private val FIELD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangNamedElement ?: return
            val type = elem.getType(null)?.readableName(elem)
            val icon = VIcons.Field

            // TODO: show fqn?
            val parent = elem.parentOfType<VlangNamedElement>()
            p.tailText = " of " + parent?.name

            p.icon = icon
            p.typeText = type
            p.isTypeGrayed = true
            p.itemText = element.lookupString
            p.isStrikeout = elem.isDeprecated()
        }
    }

    class ClassLikeRenderer(private val icon: Icon, moduleName: String?) : ElementRenderer(moduleName) {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            p.icon = icon
            p.itemText = element.lookupString
        }
    }

    class TypeAliasRenderer(moduleName: String?) : ElementRenderer(moduleName) {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangTypeAliasDeclaration ?: return
            val types = elem.aliasType?.typeUnionList?.typeList ?: emptyList()
            val tail = if (types.size == 1) {
                " = " + types.first().text
            } else {
                ""
            }

            p.icon = VIcons.Alias
            p.itemText = element.lookupString
            p.tailText = tail
            p.isStrikeout = elem.isDeprecated()
        }
    }

    class FunctionRenderer(moduleName: String?) : ElementRenderer(moduleName) {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangFunctionDeclaration ?: return
            p.icon = VIcons.Function

            val genericParameters = elem.genericParameters?.text ?: ""
            val signature = elem.getSignature()
            val parameters = signature?.parameters?.text ?: ""
            val result = signature?.result?.text

            p.tailText = parameters
            p.itemText = element.lookupString + genericParameters
            p.typeText = result
            p.isStrikeout = elem.isDeprecated()
        }
    }

    class ConstantRenderer(moduleName: String?) : ElementRenderer(moduleName) {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangConstDefinition ?: return
            p.icon = VIcons.Constant
            p.itemText = element.lookupString

            val valueText = elem.expression?.text
            p.tailText = " = $valueText"
            p.typeText = elem.expression?.getType(null)?.readableName(elem)
            p.isStrikeout = elem.isDeprecated()
        }
    }

    private val ENUM_FIELD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangEnumFieldDefinition ?: return
            p.icon = VIcons.Field
            p.itemText = element.lookupString

            val parent = elem.parent as? VlangEnumFieldDeclaration ?: return
            val valueText = parent.expression?.text
            if (valueText != null) {
                p.tailText = " = $valueText"
            }
            p.typeText = parent.expression?.getType(null)?.readableName(elem)
            p.isStrikeout = elem.isDeprecated()
        }
    }

    abstract class ElementRenderer(private val moduleName: String?) : LookupElementRenderer<LookupElement>() {
        abstract fun render(element: LookupElement, p: LookupElementPresentation)

        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            render(element, p)

            if (moduleName != null) {
                if (p.tailText.isNullOrEmpty()) {
                    p.tailText = " from $moduleName"
                } else {
                    p.tailText += " from $moduleName"
                }
            }
        }
    }
}
