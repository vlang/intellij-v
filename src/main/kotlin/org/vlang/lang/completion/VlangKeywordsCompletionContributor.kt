package org.vlang.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.*
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil.KEYWORD_PRIORITY
import org.vlang.lang.psi.*

class VlangKeywordsCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            topLevelPattern(),
            KeywordsCompletionProvider("const", "struct", "enum", "union", "interface", "fn", "import", "module", "pub", "static", "type")
        )
        extend(
            CompletionType.BASIC,
            insideBlockPattern(VlangTypes.IDENTIFIER)
                .andNot(
                    insideWithLabelStatement(VlangTypes.IDENTIFIER)
                ),
            KeywordsCompletionProvider(*BLOCK_KEYWORDS)
        )
        extend(
            CompletionType.BASIC,
            insideForStatement(VlangTypes.IDENTIFIER),
            KeywordsCompletionProvider("continue", "break")
        )
    }

    private class KeywordsCompletionProvider(private vararg val keywords: String) : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet,
        ) {
            if (VlangCompletionUtil.shouldSuppressCompletion(parameters.position)) {
                result.stopHere()
                return
            }

            if (parameters.position.parentOfType<VlangLiteralValueExpression>() != null) {
                result.stopHere()
                return
            }

            for (keyword in keywords) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(keyword).bold(), KEYWORD_PRIORITY.toDouble()
                    )
                )
            }
        }
    }

    private fun insideForStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return insideBlockPattern(tokenType)
            .inside(
                false, psiElement(VlangForStatement::class.java),
                psiElement(VlangFunctionDeclaration::class.java)
            ).andNot(
                insideWithLabelStatement(tokenType)
            )
    }

    private fun insideWithLabelStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(
                false,
                StandardPatterns.or(
                    psiElement(VlangTypes.CONTINUE_STATEMENT),
                    psiElement(VlangTypes.BREAK_STATEMENT),
                    psiElement(VlangTypes.GOTO_STATEMENT)
                ),
                psiElement(VlangFunctionDeclaration::class.java)
            )
    }

    private fun insideBlockPattern(tokenType: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(VlangBlock::class.java)
    }

    private fun topLevelPattern(): PsiElementPattern.Capture<PsiElement?> {
        return onStatementBeginning(VlangTypes.IDENTIFIER).withParent(
            StandardPatterns.or(
                psiElement(PsiErrorElement::class.java)
                    .withParent(vlangFileWithModule()), psiElement(
                    VlangFile::class.java
                )
            )
        )
    }

    private fun vlangFileWithModule(): PsiFilePattern.Capture<VlangFile?> {
        val collection = StandardPatterns.collection(PsiElement::class.java)
        val packageIsFirst = collection.first(psiElement(VlangTypes.MODULE_CLAUSE))
        return PlatformPatterns.psiFile(VlangFile::class.java).withChildren(
            collection.filter(
                StandardPatterns.not(psiElement().whitespaceCommentEmptyOrError()),
                packageIsFirst
            )
        )
    }

    private fun onStatementBeginning(vararg tokenTypes: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return psiElement().withElementType(TokenSet.create(*tokenTypes))
    }

    companion object {
        val BLOCK_EXPRESSION_KEYWORDS = arrayOf(
            "as",
            "false",
            "go",
            "if",
            "in",
            "is",
            "isreftype",
            "match",
            "none",
            "or",
            "sizeof",
            "true",
            "typeof",
            "unsafe",
        )

        val BLOCK_KEYWORDS = arrayOf(
            "asm",
            "assert",
            "atomic",
            "defer",
            "for",
            "goto",
            "mut",
            "return",
            "rlock",
            "select",
            "shared",
            "sizeof",
            "volatile",
            "__offsetof",
        ) + BLOCK_EXPRESSION_KEYWORDS
    }
}
