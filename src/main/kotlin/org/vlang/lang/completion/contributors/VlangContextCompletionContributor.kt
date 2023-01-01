package org.vlang.lang.completion.contributors

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionPatterns.cachedReferenceExpression
import org.vlang.lang.completion.VlangCompletionPatterns.insideStatementWithLabel
import org.vlang.lang.completion.VlangCompletionPatterns.referenceExpression
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.providers.ReferenceCompletionProvider
import org.vlang.lang.utils.VlangLabelUtil

class VlangContextCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, insideStatementWithLabel(), LabelCompletionProvider)
        extend(CompletionType.BASIC, referenceExpression(), ReferenceCompletionProvider)
        extend(CompletionType.BASIC, cachedReferenceExpression(), ReferenceCompletionProvider)

//        extend(CompletionType.BASIC, referenceExpression(), VlangReceiverCompletionProvider)
    }

    private object LabelCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
            val element = parameters.originalFile.findElementAt(parameters.offset)
                ?: return

            val labels = VlangLabelUtil.collectContextLabelNames(element)

            labels.forEach {
                resultSet.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it), VlangCompletionUtil.CONTEXT_KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }
}
