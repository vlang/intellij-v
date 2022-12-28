package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil

object MapInitCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val mapElement = LookupElementBuilder.create("map")
            .withTailText("[key]value{}")
            .withInsertHandler(
                VlangCompletionUtil.TemplateStringInsertHandler(
                    "[\$key$]\$value$${"{}"}",
                    true,
                    "key" to ConstantNode("string"),
                    "value" to ConstantNode("int")
                )
            )

        result.addElement(mapElement)
    }
}
