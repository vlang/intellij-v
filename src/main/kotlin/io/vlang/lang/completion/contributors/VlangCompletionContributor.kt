package io.vlang.lang.completion.contributors

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import io.vlang.lang.completion.VlangCompletionPatterns.cachedReferenceExpression
import io.vlang.lang.completion.VlangCompletionPatterns.insideStatementWithLabel
import io.vlang.lang.completion.VlangCompletionPatterns.insideStruct
import io.vlang.lang.completion.VlangCompletionPatterns.onExpression
import io.vlang.lang.completion.VlangCompletionPatterns.onModuleImportName
import io.vlang.lang.completion.VlangCompletionPatterns.onStatement
import io.vlang.lang.completion.VlangCompletionPatterns.onTopLevel
import io.vlang.lang.completion.VlangCompletionPatterns.onType
import io.vlang.lang.completion.VlangCompletionPatterns.referenceExpression
import io.vlang.lang.completion.providers.*
import io.vlang.lang.completion.sort.withVlangSorter

class VlangCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, onTopLevel(),         MethodNameCompletionProvider)
        extend(CompletionType.BASIC, onTopLevel(),         TestFunctionsCompletionProvider)
        extend(CompletionType.BASIC, onModuleImportName(), ModuleNameCompletionProvider)
        extend(CompletionType.BASIC, onStatement(),        ReturnKeywordCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       MapInitCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       ChanInitCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       AnonFunctionContextCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       EnumFieldContextCompletionProvider)
        extend(CompletionType.BASIC, onType(),             MapTypeCompletionProvider)
        extend(CompletionType.BASIC, onType(),             ChanTypeCompletionProvider)
        extend(CompletionType.BASIC, insideStruct(),       StructFieldGroupModifierCompletionProvider)

        extend(CompletionType.BASIC, insideStatementWithLabel(),  LabelCompletionProvider)
        extend(CompletionType.BASIC, referenceExpression(),       ReferenceCompletionProvider)
        extend(CompletionType.BASIC, cachedReferenceExpression(), ReferenceCompletionProvider)

        extend(CompletionType.BASIC, referenceExpression(), VlangReceiverCompletionProvider)
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, withVlangSorter(parameters, result))
    }
}
