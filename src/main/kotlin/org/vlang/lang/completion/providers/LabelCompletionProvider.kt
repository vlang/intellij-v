package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.utils.VlangLabelUtil

object LabelCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
        val element = parameters.originalFile.findElementAt(parameters.offset)
            ?: return

        val labels = VlangLabelUtil.collectContextLabelNames(element)
        labels.forEach {
            val lookupElement = LookupElementBuilder.create(it)
                .withPriority(VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY)
            resultSet.addElement(lookupElement)
        }
    }
}
