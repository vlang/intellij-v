package org.vlang.lang.completion

import com.intellij.patterns.*
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

object VlangCompletionPatterns {
    fun identifier() = insideBlockPattern(VlangTypes.IDENTIFIER)
        .andNot(
            insideWithLabelStatement(VlangTypes.IDENTIFIER)
        )
        .andNot(
            insideAttribute(VlangTypes.IDENTIFIER)
        )

    fun insideAttribute(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(false, PlatformPatterns.psiElement(VlangAttribute::class.java))
    }

    fun insideStruct() = onStatementBeginning(VlangTypes.IDENTIFIER)
        .withSuperParent(6, VlangStructType::class.java)

    fun insideBlockPattern(tokenType: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return onStatementBeginning(tokenType)
    }

    fun topLevelPattern(): PsiElementPattern.Capture<PsiElement?> {
        return onStatementBeginning(VlangTypes.IDENTIFIER).withParent(
            StandardPatterns.or(
                PlatformPatterns.psiElement().withSuperParent(3, vlangFileWithModule()),
                PlatformPatterns.psiElement().withSuperParent(3, VlangFile::class.java),
            )
        )
    }

    fun vlangFileWithModule(): PsiFilePattern.Capture<VlangFile?> {
        val collection = StandardPatterns.collection(PsiElement::class.java)
        val packageIsFirst = collection.first(PlatformPatterns.psiElement(VlangTypes.MODULE_CLAUSE))
        return PlatformPatterns.psiFile(VlangFile::class.java).withChildren(
            collection.filter(
                StandardPatterns.not(PlatformPatterns.psiElement().whitespaceCommentEmptyOrError()),
                packageIsFirst
            )
        )
    }

    fun onStatementBeginning(vararg tokenTypes: IElementType): PsiElementPattern.Capture<PsiElement?> {
        return PlatformPatterns.psiElement().withElementType(TokenSet.create(*tokenTypes))
    }

    fun insideForStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return insideBlockPattern(tokenType)
            .inside(
                false, PlatformPatterns.psiElement(VlangForStatement::class.java),
                PlatformPatterns.psiElement(VlangFunctionDeclaration::class.java)
            ).andNot(
                insideWithLabelStatement(tokenType)
            )
    }

    fun insideSqlStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(
                false, PlatformPatterns.psiElement(VlangSqlExpression::class.java),
                PlatformPatterns.psiElement(VlangFunctionDeclaration::class.java)
            )
    }

    fun insideWithLabelStatement(tokenType: IElementType): ElementPattern<out PsiElement?> {
        return onStatementBeginning(tokenType)
            .inside(
                false,
                StandardPatterns.or(
                    PlatformPatterns.psiElement(VlangTypes.CONTINUE_STATEMENT),
                    PlatformPatterns.psiElement(VlangTypes.BREAK_STATEMENT),
                    PlatformPatterns.psiElement(VlangTypes.GOTO_STATEMENT)
                ),
                PlatformPatterns.psiElement(VlangFunctionDeclaration::class.java)
            )
    }
}