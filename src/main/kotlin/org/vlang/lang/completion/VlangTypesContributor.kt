package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangTypeReferenceExpression

class VlangTypesContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangTypeReferenceExpression::class.java),
            MapTypeCompletionProvider
        )

        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangTypeReferenceExpression::class.java),
            ChanTypeCompletionProvider
        )
    }

    private object MapTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            result.addElement(
                LookupElementBuilder.create("map")
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
            )
        }
    }

    private object ChanTypeCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            result.addElement(
                LookupElementBuilder.create("chan")
                    .withIcon(VIcons.Alias)
                    .withTailText(" type")
                    .withInsertHandler(
                        VlangCompletionUtil.TemplateStringInsertHandler(
                            " \$type$",
                            true,
                            "type" to ConstantNode("int"),
                        )
                    )
            )
        }
    }
}
