package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes

class VlangModuleNameCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().afterLeaf(psiElement(VlangTypes.IMPORT)),
            ModuleNameCompletionProvider()
        )
    }

    private class ModuleNameCompletionProvider : CompletionProvider<CompletionParameters>() {
        companion object {
            val VLIB_PACKAGES = listOf(
                "arrays",
                "benchmark", "bitfield", "builtin",
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
            VLIB_PACKAGES.forEach {
                result.addElement(
                    LookupElementBuilder.create(it).withIcon(AllIcons.Nodes.Package)
                )
            }
        }
    }
}