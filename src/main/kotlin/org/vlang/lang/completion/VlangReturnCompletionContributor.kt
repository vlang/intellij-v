package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.psi.VlangSignatureOwner
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangPrimitiveTypeEx

class VlangReturnCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            VlangKeywordsCompletionContributor().identifier(),
            ReturnKeywordCompletionProvider()
        )
    }

    private inner class ReturnKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (VlangKeywordsCompletionContributor.shouldSuppress(parameters, result)) return

            val signatureOwner = parameters.position.parentOfType<VlangSignatureOwner>() ?: return
            val resultType = signatureOwner.getSignature()?.result?.type?.toEx()

            if (resultType == VlangPrimitiveTypeEx.BOOL) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create("return true").bold(),
                        VlangCompletionUtil.KEYWORD_PRIORITY.toDouble()
                    )
                )
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create("return false").bold(),
                        VlangCompletionUtil.KEYWORD_PRIORITY.toDouble()
                    )
                )
            }

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("return")
                        .withInsertHandler { ctx, item ->
                            if (resultType != null) {
                                VlangCompletionUtil.StringInsertHandler(" ", 1).handleInsert(ctx, item)
                                VlangCompletionUtil.showCompletion(ctx.editor)
                            }
                        }
                        .bold(), VlangCompletionUtil.KEYWORD_PRIORITY.toDouble()
                )
            )
        }
    }
}
