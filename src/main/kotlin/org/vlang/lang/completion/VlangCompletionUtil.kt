package org.vlang.lang.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.lookup.LookupElementRenderer
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.MODULE_NAME
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.NEED_QUALIFIER_NAME
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import javax.swing.Icon

object VlangCompletionUtil {
    const val KEYWORD_PRIORITY = 20
    const val CONTEXT_COMPLETION_PRIORITY = 35
    const val CONTEXT_KEYWORD_PRIORITY = 25
    const val NOT_IMPORTED_METHOD_PRIORITY = 4
    const val METHOD_PRIORITY = NOT_IMPORTED_METHOD_PRIORITY + 10
    const val NOT_IMPORTED_FUNCTION_PRIORITY = 3
    const val FUNCTION_PRIORITY = NOT_IMPORTED_FUNCTION_PRIORITY + 10
    const val NOT_IMPORTED_STRUCT_PRIORITY = 2
    const val STRUCT_PRIORITY = NOT_IMPORTED_STRUCT_PRIORITY + 10
    const val NOT_IMPORTED_TYPE_ALIAS_PRIORITY = 1
    const val TYPE_ALIAS_PRIORITY = NOT_IMPORTED_TYPE_ALIAS_PRIORITY + 10
    const val NOT_IMPORTED_CONSTANT_PRIORITY = 1
    const val CONSTANT_PRIORITY = NOT_IMPORTED_CONSTANT_PRIORITY + 10
    const val COMPILE_TIME_CONSTANT_PRIORITY = CONSTANT_PRIORITY + 5
    const val NOT_IMPORTED_TYPE_PRIORITY = 5
    const val TYPE_PRIORITY = NOT_IMPORTED_TYPE_PRIORITY + 10
    const val NOT_IMPORTED_TYPE_CONVERSION = 1
    const val TYPE_CONVERSION = NOT_IMPORTED_TYPE_CONVERSION + 10
    const val NOT_IMPORTED_GLOBAL_VAR_PRIORITY = 15
    const val GLOBAL_VAR_PRIORITY = NOT_IMPORTED_GLOBAL_VAR_PRIORITY + 5
    const val NOT_IMPORTED_VAR_PRIORITY = 15
    const val VAR_PRIORITY = NOT_IMPORTED_VAR_PRIORITY + 10
    const val FIELD_PRIORITY = CONTEXT_KEYWORD_PRIORITY + 1
    const val LABEL_PRIORITY = 15
    const val MODULE_PRIORITY = 20

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

    fun isCompileTimeIdentifier(element: PsiElement): Boolean {
        return element.elementType == VlangTypes.IDENTIFIER && element.text.startsWith("@")
    }

    fun shouldSuppressCompletion(element: PsiElement): Boolean {
        if (element.text == CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED) {
            return true
        }

        val parent = element.parent
        if (parent.parent is VlangVarDeclaration) {
            return true
        }

        if (parent is VlangFunctionDeclaration || parent is VlangMethodName) {
            return true
        }

        if (parent is VlangStructType || parent is VlangUnionDeclaration || parent is VlangEnumDeclaration || parent is VlangInterfaceType) {
            return true
        }

        return false
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

    fun createFunctionLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createFunctionLookupElement(
            element, name, moduleName, state,
            insertHandler = FunctionInsertHandler(moduleName),
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

    fun createMethodLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createMethodLookupElement(
            element, name,
            insertHandler = StringInsertHandler("()", 1),
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

    fun createInterfaceMethodLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createInterfaceMethodLookupElement(
            element, name, moduleName, state,
            priority = METHOD_PRIORITY,
            insertHandler = FunctionInsertHandler(null)
        )
    }

    fun createStructLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }

        val moduleName = state.get(MODULE_NAME)
        return createStructLookupElement(
            element, name, moduleName, state,
            priority = STRUCT_PRIORITY,
            insertHandler = StructInsertHandler(moduleName)
        )
    }

    fun createCompilePseudoVarLookupElement(name: String, description: String): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.create("@$name")
                .withTypeText(description)
                .withIcon(VIcons.Constant), COMPILE_TIME_CONSTANT_PRIORITY.toDouble()
        )
    }

    fun createUnionLookupElement(element: VlangNamedElement, state: ResolveState): LookupElement? =
        createClassLikeLookupElement(element, state, AllIcons.Nodes.AnonymousClass, STRUCT_PRIORITY)

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

    fun createDirectoryLookupElement(dir: PsiDirectory): LookupElementBuilder {
        return LookupElementBuilder.createWithSmartPointer(dir.name, dir).withIcon(VIcons.Directory)
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
        element: VlangNamedElement, lookupString: String, moduleName: String?, state: ResolveState,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        val qualifiedName = createQualifiedName(state, lookupString)
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(qualifiedName, element)
                .withRenderer(ClassLikeRenderer(AllIcons.Nodes.Class, moduleName))
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createFieldLookupElement(
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

    private fun createMethodLookupElement(
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

    private fun createEnumFieldLookupElement(
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

    private fun createVariableLikeLookupElement(
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

    class ModuleInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName)  {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, ".")
            context.editor.caretModel.moveToOffset(caretOffset + 1)

            showCompletion(context.editor)
        }
    }

    class FunctionInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, "()")
            context.editor.caretModel.moveToOffset(caretOffset + 1)
        }
    }

    class ConstantInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName)

    class StructInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset
            context.document.insertString(caretOffset, "{}")
            context.editor.caretModel.moveToOffset(caretOffset + 1)

            showCompletion(context.editor)
        }
    }

    class ClassLikeInsertHandler(moduleName: String?) : ElementInsertHandler(moduleName) {
        override fun handleInsertion(context: InsertionContext, item: LookupElement) {}
    }

    class StringInsertHandler(val string: String, val shift: Int) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, string)
            context.editor.caretModel.moveToOffset(caretOffset + shift)
        }
    }

    class TemplateStringInsertHandler(val string: String, vararg val variables: Pair<String, Expression>) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val template = TemplateManager.getInstance(context.project)
                .createTemplate("templateInsertHandler", "vlang", string)
            template.isToReformat = true

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
        }
    }

    private val VARIABLE_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangNamedElement ?: return
            val type = elem.getType(null)?.toEx()?.readableName(elem)
            val icon = when (elem) {
                is VlangVarDefinition            -> VIcons.Variable
                is VlangGlobalVariableDefinition -> VIcons.Variable
                is VlangParamDefinition          -> VIcons.Parameter
                is VlangReceiver                 -> VIcons.Receiver
                is VlangAnonymousFieldDefinition -> VIcons.Field
                else                             -> null
            }

            p.icon = icon
            p.typeText = type
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    private val METHOD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangMethodDeclaration ?: return
            val signature = elem.getSignature()

            val typeText = signature?.result?.text ?: "void"
            val icon = VIcons.Method

            val type = elem.receiverType.toEx().readableName(elem)
            p.tailText = signature?.parameters?.text + " of " + type

            p.icon = icon
            p.typeText = typeText
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    private val INTERFACE_METHOD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangInterfaceMethodDefinition ?: return
            val signature = elem.getSignature()

            val typeText = signature.result?.text ?: "void"
            val icon = VIcons.Method

            val parent = elem.parentOfType<VlangNamedElement>()
            p.tailText = signature.parameters.text + " of " + parent?.name

            p.icon = icon
            p.typeText = typeText
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    private val FIELD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangNamedElement ?: return
            val type = elem.getType(null)?.toEx()?.readableName(elem)
            val icon = VIcons.Field

            // TODO: show fqn?
            val parent = elem.parentOfType<VlangNamedElement>()
            p.tailText = " of " + parent?.name

            p.icon = icon
            p.typeText = type
            p.isTypeGrayed = true
            p.itemText = element.lookupString
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
                " " + types.first().text
            } else {
                ""
            }

            p.icon = AllIcons.Nodes.Alias
            p.typeText = tail
            p.itemText = element.lookupString
        }
    }

    class FunctionRenderer(moduleName: String?) : ElementRenderer(moduleName) {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangFunctionDeclaration ?: return
            p.icon = VIcons.Function
            val signature = elem.getSignature()?.text ?: ""
            p.itemText = element.lookupString + signature
        }
    }

    class ConstantRenderer(moduleName: String?) : ElementRenderer(moduleName) {
        override fun render(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangConstDefinition ?: return
            p.icon = VIcons.Constant
            p.itemText = element.lookupString

            val valueText = elem.expression?.text
            p.tailText = " = $valueText"
            p.typeText = elem.expression?.getType(null)?.toEx()?.readableName(elem)
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
            p.typeText = parent.expression?.getType(null)?.toEx()?.readableName(elem)
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