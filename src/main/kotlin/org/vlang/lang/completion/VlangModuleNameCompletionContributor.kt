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
        companion object {
            val VLIB_PACKAGES = listOf(
                "arrays",
                "benchmark", "bitfield",
                "cli", "clipboard", "compress", "context", "crypto",
                "darwin", "datatypes", "dl", "dlmalloc",
                "encoding", "eventbus",
                "flag", "fontstash",
                "gg", "gx",
                "hash",
                "io",
                "js", "json",
                "log",
                "math", "mssql", "mysql",
                "net",
                "orm", "os",
                "pg", "picoev", "picohttpparser",
                "rand", "readline", "regex", "runtime",
                "semver", "sokol", "sqlite", "stbi", "strconv", "strings", "sync", "szip",
                "term", "time", "toml",
                "v", "vweb",
                "x",
            )
        }

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

            if (qualifier.isEmpty()) {
                // TODO: remove this when VlangModulesIndex.getSubmodules() wil be return vlib modules
                VLIB_PACKAGES.forEach {
                    result.addElement(
                        LookupElementBuilder.create(it).withIcon(AllIcons.Nodes.Module)
                    )
                }
            }
        }
    }
}
