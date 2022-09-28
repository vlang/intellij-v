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
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import javax.swing.Icon

object VlangCompletionUtil {
    const val KEYWORD_PRIORITY = 20
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
    const val NOT_IMPORTED_VAR_PRIORITY = 15
    const val VAR_PRIORITY = NOT_IMPORTED_VAR_PRIORITY + 10
    const val FIELD_PRIORITY = CONTEXT_KEYWORD_PRIORITY + 1
    const val LABEL_PRIORITY = 15
    const val PACKAGE_PRIORITY = 5

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

    fun createFunctionLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createFunctionLookupElement(
            element, name,
            insertHandler = StringInsertHandler("()", 1),
            priority = FUNCTION_PRIORITY,
        )
    }

    fun createConstantLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createConstantLookupElement(
            element, name,
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

    fun createStructLookupElement(element: VlangNamedElement): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createStructLookupElement(
            element, name,
            priority = STRUCT_PRIORITY,
            insertHandler = StructInsertHandler()
        )
    }

    fun createCompilePseudoVarLookupElement(name: String, description: String): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.create("@$name")
                .withTypeText(description)
                .withIcon(VIcons.Constant), COMPILE_TIME_CONSTANT_PRIORITY.toDouble()
        )
    }

    fun createUnionLookupElement(element: VlangNamedElement): LookupElement? =
        createLookupElement(element, AllIcons.Nodes.AnonymousClass, STRUCT_PRIORITY)

    fun createEnumLookupElement(element: VlangNamedElement): LookupElement? =
        createLookupElement(element, AllIcons.Nodes.Enum, STRUCT_PRIORITY)

    fun createInterfaceLookupElement(element: VlangNamedElement): LookupElement? =
        createLookupElement(element, AllIcons.Nodes.Interface, STRUCT_PRIORITY)

    fun createTypeAliasLookupElement(element: VlangNamedElement): LookupElement? =
        createLookupElement(element, AllIcons.Nodes.Alias, TYPE_ALIAS_PRIORITY)

    fun createLookupElement(element: VlangNamedElement, icon: Icon, priority: Int): LookupElement? {
        val name = element.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createLookupElement(
            element, name, icon,
            priority = priority,
        )
    }

    private fun createLookupElement(
        element: VlangNamedElement, lookupString: String,
        icon: Icon,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withIcon(icon)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
    }

    private fun createStructLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withIcon(AllIcons.Nodes.Class)
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

    private fun createFunctionLookupElement(
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(FUNCTION_RENDERER)
                .withInsertHandler(insertHandler), priority.toDouble()
        )
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
        element: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        priority: Int = 0,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, element)
                .withRenderer(CONST_RENDERER)
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

    class StringInsertHandler(val string: String, val shift: Int) : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, string)
            context.editor.caretModel.moveToOffset(caretOffset + shift)
        }
    }

    class StructInsertHandler : InsertHandler<LookupElement> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val caretOffset = context.editor.caretModel.offset

            context.document.insertString(caretOffset, "{}")
            context.editor.caretModel.moveToOffset(caretOffset + 1)

            showCompletion(context.editor)
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

    private val VARIABLE_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangNamedElement ?: return
            val type = elem.getType(null)
            val typeText = type?.text ?: ""
            val icon = when (elem) {
                is VlangVarDefinition            -> VIcons.Variable
                is VlangParamDefinition          -> VIcons.Parameter
                is VlangReceiver                 -> VIcons.Receiver
                is VlangAnonymousFieldDefinition -> VIcons.Field
                else                             -> null
            }

            p.icon = icon
            p.typeText = typeText
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

            val parentStruct = elem.receiverType?.typeReferenceExpressionList?.firstOrNull()?.resolve() as? VlangStructDeclaration
            p.tailText = signature?.parameters?.text + " of " + parentStruct?.name

            p.icon = icon
            p.typeText = typeText
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    private val FIELD_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangNamedElement ?: return
            val type = elem.getType(null)
            val typeText = type?.text ?: ""
            val icon = VIcons.Field

            val parentStruct = elem.parentOfType<VlangStructDeclaration>()
            p.tailText = " of " + parentStruct?.name

            p.icon = icon
            p.typeText = typeText
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }

    private val FUNCTION_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangFunctionDeclaration ?: return
            p.icon = VIcons.Function
            p.itemText = element.lookupString
            p.tailText = elem.getSignature()?.text
        }
    }

    private val CONST_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val elem = element.psiElement as? VlangConstDefinition ?: return
            p.icon = VIcons.Constant
            p.itemText = element.lookupString

            val valueText = elem.expression?.text
            p.tailText = " = $valueText"
            p.typeText = elem.expression?.getType(null)?.text
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
            p.typeText = parent.expression?.getType(null)?.text
        }
    }
}
