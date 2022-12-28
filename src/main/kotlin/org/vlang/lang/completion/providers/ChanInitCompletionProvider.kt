package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil

object ChanInitCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val chanElement = LookupElementBuilder.create("chan")
            .withTailText(" type{}")
            .withInsertHandler(
                VlangCompletionUtil.TemplateStringInsertHandler(
                    " \$type$${"{}"}",
                    true,
                    "type" to ConstantNode("int"),
                )
            )

        result.addElement(chanElement)
    }
}
