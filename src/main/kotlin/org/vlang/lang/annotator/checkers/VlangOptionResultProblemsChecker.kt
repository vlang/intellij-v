package org.vlang.lang.annotator.checkers

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangOptionTypeEx
import org.vlang.lang.psi.types.VlangResultTypeEx

class VlangOptionResultProblemsChecker(holder: AnnotationHolder) : VlangCheckerBase(holder) {
    override fun visitOrBlockExpr(expr: VlangOrBlockExpr) {
        val leftExpr = expr.expression ?: return

        // a[10] or {} - is valid
        if (leftExpr is VlangIndexOrSliceExpr) {
            return
        }

        // sql db { ... } or {} - is valid
        if (leftExpr is VlangSqlExpression) {
            return
        }

        // <-ch or {} - is valid
        if (expr.parent is VlangUnaryExpr && (expr.parent as VlangUnaryExpr).sendChannel != null) {
            return
        }

        val leftType = leftExpr.getType(null) ?: return

        if (leftType !is VlangResultTypeEx && leftType !is VlangOptionTypeEx) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Left expression of 'or' operator must be 'Option' or 'Result' type or '<-chan' or 'arr[i]' expression"
            )
                .range(expr.or)
                .create()
        }
    }

    override fun visitIfExpression(expr: VlangIfExpression) {
        val guard = expr.guardVarDeclaration ?: return
        val rightExpr = guard.expression ?: return
        val rightType = rightExpr.getType(null) ?: return

        // a[10] or {} - is valid
        if (rightExpr is VlangIndexOrSliceExpr) {
            return
        }

        // sql db { ... } or {} - is valid
        if (rightExpr is VlangSqlExpression) {
            return
        }

        // <-ch or {} - is valid
        if (rightExpr is VlangUnaryExpr && rightExpr.sendChannel != null) {
            return
        }

        if (rightType !is VlangResultTypeEx && rightType !is VlangOptionTypeEx) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot unwrap non-Option and non-Result type in 'if' expression"
            )
                .range(rightExpr)
                .create()
        }
    }

    override fun visitCallExprWithPropagate(expr: VlangCallExprWithPropagate) {
        val called = expr.resolve() ?: return
        if (called !is VlangSignatureOwner) {
            return
        }

        val signature = called.getSignature() ?: return
        val resultType = signature.result?.type.toEx()
        val expectedResult = expr.not != null

        if (resultType is VlangOptionTypeEx && expectedResult) {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                "Use '?' to propagate the Option instead of '!'"
            )
                .range(expr)
                .create()
        }

        if (resultType is VlangResultTypeEx && !expectedResult) {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                "Use '!' to propagate the Result instead of '?'"
            )
                .range(expr)
                .create()
        }

        if (resultType !is VlangResultTypeEx && resultType !is VlangOptionTypeEx) {
            val mark = if (expectedResult) "!" else "?"
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot use propagation '$mark' for non-Option and non-Result type"
            )
                .range(expr)
                .create()
        }
    }

    override fun visitReferenceExpression(expr: VlangReferenceExpression) {
        val reference = expr.reference
        val resolved = reference.multiResolve(false).firstOrNull()?.element ?: return

        if (resolved is VlangConstDefinition && resolved.name == "err" && resolved.getModuleName() == VlangCodeInsightUtil.STUBS_MODULE) {
            checkErrVariableUsage(expr, holder)
        }
    }

    private fun checkErrVariableUsage(element: PsiElement, holder: AnnotationHolder) {
        val owner = VlangLangUtil.getErrVariableOwner(element) ?: return
        val typeElement = when (owner) {
            is VlangOrBlockExpr -> owner.expression
            is VlangIfExpression -> owner.guardVarDeclaration?.expression
            else -> null
        } ?: return

        val type = typeElement.getType(null) ?: return
        if (type !is VlangResultTypeEx) {
            val action = if (typeElement is VlangCallExpr) "returns" else "has"
            holder.newAnnotation(
                HighlightSeverity.WEAK_WARNING,
                "'err' is always 'none', since '${typeElement.text}' $action non-Result type"
            )
                .create()
        }
    }
}
