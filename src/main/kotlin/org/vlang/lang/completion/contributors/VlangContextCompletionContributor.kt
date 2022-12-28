package org.vlang.lang.completion.contributors

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionPatterns.insideStatementWithLabel
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.providers.ReferenceCompletionProvider
import org.vlang.lang.psi.VlangReferenceExpressionBase
import org.vlang.lang.psi.impl.VlangCachedReference
import org.vlang.lang.utils.LabelUtil

class VlangContextCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, insideStatementWithLabel(), LabelCompletionProvider)
        extend(CompletionType.BASIC, referenceExpression(), ReferenceCompletionProvider)
        extend(CompletionType.BASIC, cachedReferenceExpression(), ReferenceCompletionProvider)

//        extend(
//            CompletionType.BASIC,
//            referenceExpression(),
//            VlangReceiverCompletionProvider()
//        )
    }

    private object LabelCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
            val element = parameters.originalFile.findElementAt(parameters.offset)
                ?: return

            val labels = LabelUtil.collectContextLabelNames(element)

            labels.forEach {
                resultSet.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(it), VlangCompletionUtil.CONTEXT_KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    private fun referenceExpression(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withParent(VlangReferenceExpressionBase::class.java)
    }

    private fun cachedReferenceExpression(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withParent(psiElement().withReference(VlangCachedReference::class.java))
    }
}
