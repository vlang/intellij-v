package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.VlangUnionDeclaration
import org.vlang.lang.stubs.index.*

class VlangCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            KeywordsCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            VlangNamesCompletionProvider()
        )
    }

    class VlangNamesCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val file = parameters.originalFile

            val allPublicNames = VlangNamesIndex.getAll(file.project)

            allPublicNames.forEach {
                var el = LookupElementBuilder.create(it)

                when (it) {
                    is VlangFunctionDeclaration -> {
                        el = el
                            .appendTailText(it.getSignature()?.text ?: "()", true)
                            .withIcon(AllIcons.Nodes.Function)
                            .withInsertHandler(NyInsertHandler())
                    }
                    is VlangStructDeclaration -> {
                        el = el
                            .withIcon(AllIcons.Nodes.Class)
                    }
                    is VlangUnionDeclaration -> {
                        el = el
                            .withIcon(AllIcons.Nodes.AnonymousClass)
                    }
                    is VlangEnumDeclaration -> {
                        el = el
                            .withIcon(AllIcons.Nodes.Enum)
                    }
                    is VlangTypeAliasDeclaration -> {
                        el = el
                            .withIcon(AllIcons.Nodes.Alias)
                    }
                }

                resultSet.addElement(el)
            }
        }

        class NyInsertHandler : InsertHandler<LookupElement> {
            override fun handleInsert(context: InsertionContext, item: LookupElement) {
                val caretOffset = context.editor.caretModel.offset

                context.document.insertString(caretOffset, "()")
                context.editor.caretModel.moveToOffset(caretOffset + 1)
            }
        }
    }

    private class KeywordsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            for (keyword in KEYWORDS) {
                result.addElement(LookupElementBuilder.create(keyword).bold())
            }
        }

        companion object {
            val KEYWORDS = listOf(
                "as",
                "asm",
                "assert",
                "atomic",
                "break",
                "const",
                "continue",
                "defer",
                "else",
                "enum",
                "false",
                "fn",
                "for",
                "go",
                "goto",
                "if",
                "import",
                "in",
                "interface",
                "is",
                "isreftype",
                "lock",
                "match",
                "module",
                "mut",
                "none",
                "or",
                "pub",
                "return",
                "rlock",
                "select",
                "shared",
                "sizeof",
                "static",
                "struct",
                "true",
                "type",
                "typeof",
                "union",
                "unsafe",
                "volatile",
                "__offsetof",
            )
        }
    }
}
