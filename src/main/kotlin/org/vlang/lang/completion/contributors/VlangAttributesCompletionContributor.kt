package org.vlang.lang.completion.contributors

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionPatterns.onAttributeIdentifier
import org.vlang.lang.completion.VlangCompletionPatterns.onFieldAttributeIdentifier
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.VlangFieldDeclaration

class VlangAttributesCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, onAttributeIdentifier(), CommonAttributeCompletionProvider)
        extend(CompletionType.BASIC, onAttributeIdentifier(), AttributeWithColonCompletionProvider)
        extend(CompletionType.BASIC, onFieldAttributeIdentifier(), JsonAttributeCompletionProvider)
    }

    private object CommonAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = ATTRIBUTES.map {
                LookupElementBuilder.create(it)
                    .withIcon(AllIcons.Nodes.ObjectTypeAttribute)
            }

            result.addAllElements(elements)
        }

        private val ATTRIBUTES = listOf(
            "params", "noinit", "required", "skip", "assert_continues",
            "unsafe", "manualfree", "heap", "nonnull", "primary", "inline",
            "direct_array_access", "live", "flag", "noinline", "noreturn",
            "typedef", "console", "keep_args_alive",
        )
    }

    private object AttributeWithColonCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val elements = ATTRIBUTES.map {
                LookupElementBuilder.create(it)
                    .withIcon(AllIcons.Nodes.ObjectTypeAttribute)
                    .withTailText(": 'value'", true)
                    .withInsertHandler(INSERT_HANDLER)
            }

            result.addAllElements(elements)
        }

        val ATTRIBUTES = listOf("sql", "table", "deprecated", "deprecated_after", "export", "callconv")

        val INSERT_HANDLER = InsertHandler<LookupElement> { context, _ ->
            val offset = context.tailOffset
            context.document.insertString(offset, ": ''")
            context.editor.caretModel.moveToOffset(offset + 3)
        }
    }

    private object JsonAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val element = LookupElementBuilder.create("json")
                .withIcon(AllIcons.Nodes.ObjectTypeAttribute)
                .withInsertHandler(INSERT_HANDLER)

            result.addElement(element)
        }

        val INSERT_HANDLER = InsertHandler<LookupElement> { context, item ->
            val offset = context.tailOffset
            val element = context.file.findElementAt(offset)
            val fieldDeclaration = element?.parentOfType<VlangFieldDeclaration>()

            if (fieldDeclaration != null) {
                val fieldName = fieldDeclaration.fieldDefinition?.name ?: ""
                VlangCompletionUtil.TemplateStringInsertHandler(
                    ": '\$name$'", true,
                    "name" to ConstantNode(fieldName)
                ).handleInsert(context, item)
            }
        }
    }
}
