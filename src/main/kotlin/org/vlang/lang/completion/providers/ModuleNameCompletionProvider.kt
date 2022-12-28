package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.stubs.index.VlangModulesIndex

object ModuleNameCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val element = parameters.position.parent as? VlangImportName ?: return
        val qualifier = element.qualifier

        val modules = if (qualifier.isNotEmpty())
            VlangModulesIndex.getSubmodules(element.project, qualifier)
        else
            VlangModulesIndex.getAll(element.project)

        val elements = modules
            .mapNotNull { it.getModuleName() }
            .map {
                LookupElementBuilder.create(it)
                    .withIcon(AllIcons.Nodes.Module)
            }

        result.addAllElements(elements)
    }
}
