package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.psi.VlangSignatureOwner
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangPrimitiveTypeEx

object ReturnKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val signatureOwner = parameters.position.parentOfType<VlangSignatureOwner>() ?: return
        val resultType = signatureOwner.getSignature()?.result?.type?.toEx()

        if (resultType == VlangPrimitiveTypeEx.BOOL) {
            val returnTrue = LookupElementBuilder.create("return true")
                .bold()
                .withPriority(VlangCompletionUtil.KEYWORD_PRIORITY)

            val returnFalse = LookupElementBuilder.create("return false")
                .bold()
                .withPriority(VlangCompletionUtil.KEYWORD_PRIORITY)

            result.addElement(returnTrue)
            result.addElement(returnFalse)
        }

        val returnElement = LookupElementBuilder.create("return")
            .withInsertHandler { ctx, item ->
                if (resultType != null) {
                    VlangCompletionUtil.StringInsertHandler(" ", 1).handleInsert(ctx, item)
                    VlangCompletionUtil.showCompletion(ctx.editor)
                }
            }
            .bold()
            .withPriority(VlangCompletionUtil.KEYWORD_PRIORITY)

        result.addElement(returnElement)
    }
}
