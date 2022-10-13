package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangPlainAttribute

class VlangCommonAttributesCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangPlainAttribute::class.java),
            CommonAttributeCompletionProvider()
        )
    }

    private class CommonAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
        companion object {
            private val ATTRIBUTES = listOf(
                "deprecated", "deprecated_after", "params", "noinit", "required", "skip", "assert_continues",
                "unsafe", "manualfree", "heap", "nonnull", "primary", "sql", "export", "inline", "json", "callconv",
                "direct_array_access", "live", "flag", "noinline", "noreturn", "typedef", "console", "table"
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
}
