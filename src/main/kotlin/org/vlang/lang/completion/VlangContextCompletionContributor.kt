package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangReferenceExpressionBase
import org.vlang.lang.psi.impl.VlangCachedReference
import org.vlang.lang.utils.LabelUtil

class VlangContextCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            insideWithLabelStatement(VlangTypes.IDENTIFIER),
            LabelCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            referenceExpression(),
            ReferenceCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            cachedReferenceExpression(),
            ReferenceCompletionProvider()
        )
    }

    private class LabelCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet,
        ) {
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

    private fun onStatementBeginning(vararg tokenTypes: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return psiElement().withElementType(TokenSet.create(*tokenTypes))
    }

    private fun insideWithLabelStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(
                false,
                StandardPatterns.or(
                    psiElement(VlangTypes.CONTINUE_STATEMENT),
                    psiElement(VlangTypes.BREAK_STATEMENT),
                    psiElement(VlangTypes.GOTO_STATEMENT)
                ),
                psiElement(VlangFunctionDeclaration::class.java)
            )
    }

}
