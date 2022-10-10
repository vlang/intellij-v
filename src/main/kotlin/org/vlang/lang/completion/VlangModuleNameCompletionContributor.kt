package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.stubs.index.VlangModulesIndex

class VlangModuleNameCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(VlangTypes.IDENTIFIER).withParent(VlangImportName::class.java),
            ModuleNameCompletionProvider()
        )
    }

    private class ModuleNameCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val element = parameters.position.parent as? VlangImportName ?: return
            val qualifier = element.qualifier

            val modules = if (qualifier.isNotEmpty())
                VlangModulesIndex.getSubmodules(element.project, qualifier)
            else
                VlangModulesIndex.getAll(element.project)

            result.addAllElements(
                modules.mapNotNull {
                    val name = it.getModuleName() ?: return@mapNotNull null
                    LookupElementBuilder.create(name)
                        .withIcon(AllIcons.Nodes.Module)
                }
            )
        }
    }
}
