package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangAttributeIdentifier
import org.vlang.lang.psi.VlangFieldDeclaration

class VlangAttributesCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangAttributeIdentifier::class.java),
            CommonAttributeCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER)
                .withParent(VlangAttributeIdentifier::class.java)
                .inside(VlangFieldDeclaration::class.java),
            JsonAttributeCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangAttributeIdentifier::class.java),
            AttributeWithColonCompletionProvider("sql", "table", "deprecated", "deprecated_after", "export", "callconv")
        )
    }

    private class CommonAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
        companion object {
            private val ATTRIBUTES = listOf(
                "params", "noinit", "required", "skip", "assert_continues",
                "unsafe", "manualfree", "heap", "nonnull", "primary", "inline",
                "direct_array_access", "live", "flag", "noinline", "noreturn", "typedef", "console",
            )
        }

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            result.addAllElements(
                ATTRIBUTES.map {
                    LookupElementBuilder.create(it).withIcon(AllIcons.Nodes.ObjectTypeAttribute)
                }
            )
        }
    }

    private class AttributeWithColonCompletionProvider(vararg val names: String) : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            result.addAllElements(
                names.map {
                    LookupElementBuilder.create(it)
                        .withIcon(AllIcons.Nodes.ObjectTypeAttribute)
                        .withTailText(": 'value'", true)
                        .withInsertHandler(INSERT_HANDLER)
                }
            )
        }

        companion object {
            val INSERT_HANDLER = InsertHandler<LookupElement> { context, item ->
                val offset = context.tailOffset
                context.document.insertString(offset, ": ''")
                context.editor.caretModel.moveToOffset(offset + 3)
            }
        }
    }

    private class JsonAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val element = LookupElementBuilder.create("json")
                .withIcon(AllIcons.Nodes.ObjectTypeAttribute)
                .withInsertHandler(INSERT_HANDLER)

            result.addElement(element)
        }

        companion object {
            val INSERT_HANDLER = InsertHandler<LookupElement> { context, item ->
                val offset = context.tailOffset
                val element = context.file.findElementAt(offset)
                val fieldDeclaration = element?.parentOfType<VlangFieldDeclaration>()

                if (fieldDeclaration != null) {
                    val fieldName = fieldDeclaration.fieldDefinition?.name ?: ""
                    VlangCompletionUtil.TemplateStringInsertHandler(
                        ": '\$name$'", true,
                        "name" to ConstantNode(fieldName)
                    )
                        .handleInsert(context, item)
                }
            }
        }
    }
}
