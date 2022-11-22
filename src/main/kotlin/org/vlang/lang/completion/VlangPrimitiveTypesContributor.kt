package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangType
import org.vlang.lang.psi.types.VlangPrimitiveTypes

class VlangPrimitiveTypesContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).inside(VlangType::class.java),
            PrimitiveTypesCompletionProvider()
        )
    }

    private class PrimitiveTypesCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            VlangPrimitiveTypes.values().forEach {
                result.addElement(
                    LookupElementBuilder.create(it.value)
                        .withIcon(VIcons.Alias)
                )
            }
        }
    }
}
