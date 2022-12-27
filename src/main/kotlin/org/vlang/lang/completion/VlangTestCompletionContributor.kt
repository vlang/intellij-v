package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionPatterns.identifier
import org.vlang.lang.psi.VlangFile

class VlangTestCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, identifier(), SpecialTestFunctionsCompletionProvider)
    }

    private object SpecialTestFunctionsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            if (VlangKeywordsCompletionContributor.shouldSuppress(parameters, result)) return

            val file = parameters.originalFile as? VlangFile ?: return
            if (!file.isTestFile()) return

            val functions = file.getFunctions()
            val containsTestsuiteBegin = functions.any { it.name == "testsuite_begin" }
            val containsTestsuiteEnd = functions.any { it.name == "testsuite_end" }

            if (!containsTestsuiteBegin) {
                result.addElement(fnPrefixedElement("testsuite_begin"))
            }

            if (!containsTestsuiteEnd) {
                result.addElement(fnPrefixedElement("testsuite_end"))
            }
        }

        private fun fnPrefixedElement(name: String) = PrioritizedLookupElement.withPriority(
            LookupElementBuilder.create("fn $name")
                .withTailText("() {...}")
                .withInsertHandler(VlangCompletionUtil.TemplateStringInsertHandler("() {\n\t\$END$\n}"))
                .bold(),
            VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY.toDouble()
        )
    }
}
