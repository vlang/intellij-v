package org.vlang.lang.completion

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.vlang.lang.VlangTypes.*
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangCachedReference
import org.vlang.utils.parentNth

object VlangCompletionPatterns {
    private val whitespace: PsiElementPattern.Capture<PsiElement> = psiElement().whitespace()

    /**
     * Expressions like:
     *
     *     a = unsafe { ... }
     */
    fun onExpression(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withParent(VlangReferenceExpression::class.java)
            .notAfterDot()
            .notAfterLiteral()
            .notInsideArrayInit()
            .notInsideStructInitWithKeys()
            .notInsideTrailingStruct()

    /**
     * Statements like:
     *
     *     defer {}
     */
    fun onStatement(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withSuperParent(2, VlangLeftHandExprList::class.java)
            .withSuperParent(3, VlangSimpleStatement::class.java)
            .notAfterDot()
            .notAfterLiteral()

    /**
     * Any top-level statements like:
     *
     *     import mod
     */
    fun onTopLevel(): PsiElementPattern.Capture<PsiElement?> =
        onStatement().withSuperParent(4, VlangFile::class.java)

    /**
     * Any place where type expected like:
     *
     *    fn foo() <caret>
     */
    fun onType(): PsiElementPattern.Capture<PsiElement> =
        psiElement().withParent(VlangTypeReferenceExpression::class.java)

    /**
     * Element after if/else expression like:
     *
     *    if true { ... } <caret>
     */
    fun onIfElse(): PsiElementPattern.Capture<PsiElement> {
        val braceAfterIf = psiElement(RBRACE).withSuperParent(2, psiElement(IF_EXPRESSION))
        return psiElement().afterLeafSkipping(whitespace, braceAfterIf)
    }

    /**
     * Element after $if/$else expression like:
     *
     *    $if true { ... } <caret>
     */
    fun onCompileTimeIfElse(): PsiElementPattern.Capture<PsiElement> {
        val braceAfterIf = psiElement(RBRACE).withSuperParent(2, psiElement(COMPILE_TIME_IF_EXPRESSION))
        return psiElement().afterLeafSkipping(whitespace, braceAfterIf)
    }

    /**
     * Element after match arm like:
     *
     *    match {
     *      100 { ... }
     *      <caret>
     *    }
     */
    fun onMatchElse(): PsiElementPattern.Capture<PsiElement> {
        val braceAfterMatch = psiElement(RBRACE).withSuperParent(4, psiElement(MATCH_EXPRESSION))
        return psiElement().afterLeafSkipping(whitespace, braceAfterMatch)
    }

    /**
     * Element after import keyword like:
     *
     *    import <caret>
     */
    fun onModuleImportName(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withParent(VlangImportName::class.java)
    }

    /**
     * Element inside attribute like:
     *
     *    [<caret>]
     */
    fun onAttributeIdentifier(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withParent(VlangAttributeIdentifier::class.java)
    }

    /**
     * Element inside attribute inside struct field like:
     *
     *    struct A {
     *      field string [<caret>]
     *    }
     */
    fun onFieldAttributeIdentifier(): PsiElementPattern.Capture<PsiElement> {
        return psiElement()
            .withParent(VlangAttributeIdentifier::class.java)
            .inside(VlangFieldDeclaration::class.java)
    }

    fun insideStruct(): PsiElementPattern.Capture<PsiElement> =
        psiElement()
            .withSuperParent(6, VlangStructType::class.java)

    fun insideForStatement(): ElementPattern<out PsiElement?> {
        return onStatement()
            .inside(VlangForStatement::class.java)
    }

    fun insideSqlStatement(): ElementPattern<out PsiElement?> {
        return psiElement(IDENTIFIER)
            .inside(VlangSqlExpression::class.java)
    }

    fun insideStatementWithLabel(): ElementPattern<out PsiElement?> {
        return psiElement()
            .inside(
                or(
                    psiElement(CONTINUE_STATEMENT),
                    psiElement(BREAK_STATEMENT),
                    psiElement(GOTO_STATEMENT)
                ),
            )
    }

    fun referenceExpression(): PsiElementPattern.Capture<PsiElement> {
        return psiElement()
            .withParent(VlangReferenceExpressionBase::class.java)
            .notAfterLiteral()
    }

    fun cachedReferenceExpression(): PsiElementPattern.Capture<PsiElement> {
        return psiElement()
            .withParent(psiElement().withReference(VlangCachedReference::class.java))
            .notAfterLiteral()
    }

    private fun PsiElementPattern.Capture<PsiElement>.notAfterDot(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().afterLeafSkipping(whitespace, psiElement(DOT)))
    }

    private fun PsiElementPattern.Capture<PsiElement>.notAfterLiteral(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notAfterLiteral") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                // when autocomplete in position: `100<caret>`
                // then `t` is `intellijrulezzz` and prev leaf is error element, because
                // `100intellijrulezzz` is not a valid expression,
                // so we need to check that prev leaf is error element
                // and prev leaf of error element is literal
                // and return true to disable completion
                // true because this matcher used in andNot() matcher.
                val prevLeaf = PsiTreeUtil.prevLeaf(t) as? PsiErrorElement ?: return false
                return PsiTreeUtil.prevLeaf(prevLeaf)?.parent is VlangLiteral
            }
        }))
    }

     private fun PsiElementPattern.Capture<PsiElement>.notInsideArrayInit(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notInsideArrayInit") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                val element = t.parentNth<VlangElement>(3) ?: return false
                // if 'key: <caret>'
                if (element.key != null) return false
                val parent = element.parent as? VlangLiteralValueExpression
                return parent != null && parent.type is VlangArrayType
            }
        }))
    }

    private fun PsiElementPattern.Capture<PsiElement>.notInsideStructInitWithKeys(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notInsideStructInitWithKeys") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                val element = t.parentNth<VlangElement>(3) ?: return false
                // if 'key: <caret>'
                if (element.key != null) return false

                // foo(fn<caret>)
                if (element.parent is VlangArgumentList) {
                    return false
                }

                val prevElement = PsiTreeUtil.findSiblingBackward(element, ELEMENT, null) as? VlangElement
                // if 'value, <caret>'
                if (prevElement != null && prevElement.key == null) return false

                return true
            }
        }))
    }

    private fun PsiElementPattern.Capture<PsiElement>.notInsideTrailingStruct(): PsiElementPattern.Capture<PsiElement> {
        return andNot(psiElement().with(object : PatternCondition<PsiElement>("notInsideTrailingStruct") {
            override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
                val element = t.parentNth<VlangElement>(3) ?: return false
                // if 'key: <caret>'
                if (element.key != null) return false

                // foo(fn<caret>)
                if (element.parent !is VlangArgumentList) {
                    return false
                }

                val prevElement = PsiTreeUtil.findSiblingBackward(element, ELEMENT, null) as? VlangElement ?: return false
                // if 'value, <caret>'
                if (prevElement.key == null) return false

                return true
            }
        }))
    }
}
