package org.vlang.lang.completion.contributors

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import org.vlang.lang.completion.VlangCompletionPatterns.onExpression
import org.vlang.lang.completion.VlangCompletionPatterns.onModuleImportName
import org.vlang.lang.completion.VlangCompletionPatterns.onStatement
import org.vlang.lang.completion.VlangCompletionPatterns.onTopLevel
import org.vlang.lang.completion.VlangCompletionPatterns.onType
import org.vlang.lang.completion.providers.*

class VlangCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, onTopLevel(),         MethodNameCompletionProvider)
        extend(CompletionType.BASIC, onTopLevel(),         SpecialTestFunctionsCompletionProvider)
        extend(CompletionType.BASIC, onModuleImportName(), ModuleNameCompletionProvider)
        extend(CompletionType.BASIC, onStatement(),        ReturnKeywordCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       MapInitCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       ChanInitCompletionProvider)
        extend(CompletionType.BASIC, onExpression(),       AnonFunctionContextCompletionProvider)
        extend(CompletionType.BASIC, onType(),             MapTypeCompletionProvider)
        extend(CompletionType.BASIC, onType(),             ChanTypeCompletionProvider)
    }
}
