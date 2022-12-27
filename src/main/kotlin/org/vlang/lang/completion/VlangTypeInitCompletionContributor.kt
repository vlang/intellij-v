package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.impl.VlangPsiImplUtil

class VlangTypeInitCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangReferenceExpression::class.java),
            MapInitCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangReferenceExpression::class.java),
            ChanInitCompletionProvider
        )
    }

    private object MapInitCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val pos = parameters.position
            if (VlangPsiImplUtil.prevDot(pos)) {
                return
            }

            result.addElement(
                LookupElementBuilder.create("map")
                    .withTailText("[key]value{}")
                    .withInsertHandler(
                        VlangCompletionUtil.TemplateStringInsertHandler(
                            "[\$key$]\$value$${"{}"}",
                            true,
                            "key" to ConstantNode("string"),
                            "value" to ConstantNode("int")
                        )
                    )
            )
        }
    }

    private object ChanInitCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val pos = parameters.position
            if (VlangPsiImplUtil.prevDot(pos)) {
                return
            }

            result.addElement(
                LookupElementBuilder.create("chan")
                    .withTailText(" type{}")
                    .withInsertHandler(
                        VlangCompletionUtil.TemplateStringInsertHandler(
                            " \$type$${"{}"}",
                            true,
                            "type" to ConstantNode("int"),
                        )
                    )
            )
        }
    }
}
