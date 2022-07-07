package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.stubs.index.VlangFunctionIndex
import org.vlang.lang.stubs.index.VlangStructIndex
import org.vlang.lang.stubs.index.VlangTypeAliasIndex
import org.vlang.lang.stubs.index.VlangUnionIndex

class VlangCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            VlangFunctionsCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            KeywordsCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            VlangStructsCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            VlangUnionsCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VlangTypes.IDENTIFIER),
            VlangTypeAliasesCompletionProvider()
        )
    }

    class VlangFunctionsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val file = parameters.originalFile
            val allFunctions = VlangFunctionIndex.getAll(file.project)

            allFunctions.forEach {
                val el = LookupElementBuilder.create(it)
                    .appendTailText(it.getSignature()?.text ?: "()", true)
                    .withIcon(AllIcons.Nodes.Function)
                    .withInsertHandler(NyInsertHandler())
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

    class VlangStructsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val file = parameters.originalFile
            val allStructs = VlangStructIndex.getAll(file.project)

            allStructs.forEach {
                val el = LookupElementBuilder.create(it)
                    .withIcon(AllIcons.Nodes.Class)
                resultSet.addElement(el)
            }
        }
    }

    class VlangUnionsCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val file = parameters.originalFile
            val allStructs = VlangUnionIndex.getAll(file.project)

            allStructs.forEach {
                val el = LookupElementBuilder.create(it)
                    .withIcon(AllIcons.Nodes.AnonymousClass)
                resultSet.addElement(el)
            }
        }
    }

    class VlangTypeAliasesCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            resultSet: CompletionResultSet
        ) {
            val file = parameters.originalFile
            val allStructs = VlangTypeAliasIndex.getAll(file.project)

            allStructs.forEach {
                val el = LookupElementBuilder.create(it)
                    .withIcon(AllIcons.Nodes.Alias)
                resultSet.addElement(el)
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
