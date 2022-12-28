package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.util.ProcessingContext
import org.vlang.ide.ui.VIcons
import org.vlang.lang.completion.VlangCompletionUtil

object MapTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val element = LookupElementBuilder.create("map")
            .withIcon(VIcons.Alias)
            .withTailText("[key]value")
            .withInsertHandler(
                VlangCompletionUtil.TemplateStringInsertHandler(
                    "[\$key$]\$value$",
                    true,
                    "key" to ConstantNode("string"),
                    "value" to ConstantNode("int")
                )
            )

        result.addElement(element)
    }
}
