package io.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext
import io.vlang.ide.codeInsight.VlangTypeInferenceUtil
import io.vlang.lang.completion.VlangCompletionUtil
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import io.vlang.lang.psi.types.VlangEnumTypeEx

object EnumFieldContextCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val element = parameters.position.parent
        val contextType = VlangTypeInferenceUtil.getContextType(element)?.unwrapAlias() ?: return
        if (contextType is VlangEnumTypeEx) {
            val resolved = contextType.resolve(element.project) ?: return
            val variants = resolved.enumType.fieldList.map {
                VlangCompletionUtil.createEnumFieldLookupElement(
                    it, ".${it.name}",
                    priority = VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY,
                )
            }

            result.addAllElements(variants)
        }
    }
}
