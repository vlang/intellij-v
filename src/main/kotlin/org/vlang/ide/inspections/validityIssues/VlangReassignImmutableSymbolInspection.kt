package org.vlang.ide.inspections.validityIssues

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.utils.VlangUnsafeUtil

class VlangReassignImmutableSymbolInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitAssignmentStatement(o: VlangAssignmentStatement) {
                super.visitAssignmentStatement(o)
                if (VlangUnsafeUtil.insideUnsafe(o)) return

                val leftExprs = o.leftHandExprList.expressionList
                for (leftExpr in leftExprs) {
                    val expr = extractExpressionToCheck(leftExpr) ?: continue
                    checkReferenceExpression(
                        expr,
                        { kind, name ->
                            if (kind == "constant") "Constant '$name' cannot be reassigned"
                            else "Immutable $kind '$name' cannot be reassigned"
                        },
                        { kind, name ->
                            if (kind == "constant") "Immutably captured constant '$name' cannot be reassigned"
                            else "Immutably captured $kind '$name' cannot be reassigned"
                        },
                    )
                }
            }

            fun extractExpressionToCheck(expr: VlangExpression?): VlangReferenceExpression? {
                return when (expr) {
                    is VlangIndexOrSliceExpr    -> extractExpressionToCheck(expr.expressionList.firstOrNull())
                    is VlangParenthesesExpr     -> extractExpressionToCheck(expr.expression)
                    is VlangReferenceExpression -> expr
                    else                        -> null
                }
            }

            override fun visitIncDecExpression(expr: VlangIncDecExpression) {
                if (VlangUnsafeUtil.insideUnsafe(expr)) return

                val leftExpr = expr.expression
                if (leftExpr !is VlangReferenceExpression) return
                checkReferenceExpression(
                    leftExpr,
                    { kind, name -> "Cannot increment/decrement immutable $kind '$name'" },
                    { kind, name -> "Cannot increment/decrement immutably captured $kind '$name'" },
                )
            }

            override fun visitAppendStatement(stmt: VlangAppendStatement) {
                if (VlangUnsafeUtil.insideUnsafe(stmt)) return

                val leftExpr = stmt.left ?: return
                if (leftExpr !is VlangReferenceExpression) return
                checkReferenceExpression(
                    leftExpr,
                    { kind, name -> "Cannot append to immutable $kind '$name'" },
                    { kind, name -> "Cannot append to immutably captured $kind '$name'" },
                )
            }

            override fun visitRangeClause(clause: VlangRangeClause) {
                val mutable = clause.variablesList.any { it.isMutable() }
                if (!mutable) return

                val rightExpr = clause.expression ?: return
                checkReferenceExpression(
                    rightExpr,
                    { kind, name -> "Cannot have mutable variable when iterate over immutable $kind '$name'" },
                    { kind, name -> "Cannot have mutable variable when iterate over immutably captured $kind '$name'" },
                )
            }

            private fun checkReferenceExpression(
                leftExpr: VlangExpression?,
                message: (kind: String, name: String?) -> String,
                captureVariableMessage: (kind: String, name: String?) -> String = message,
            ) {
                if (leftExpr == null) return

                visitQualifier(leftExpr) { qualifier, symbol ->
                    if (symbol !is VlangNamedElement) return@visitQualifier

                    val kind = when (symbol) {
                        is VlangVarDefinition   -> "variable"
                        is VlangReceiver        -> "receiver"
                        is VlangParamDefinition -> "parameter"
                        is VlangFieldDefinition -> "field"
                        is VlangConstDefinition -> "constant"
                        else                    -> return@visitQualifier
                    }

                    val quickFix = when (symbol) {
                        is VlangVarDefinition   -> listOf(MAKE_MUTABLE_QUICK_FIX)
                        is VlangReceiver        -> listOf(MAKE_MUTABLE_QUICK_FIX)
                        is VlangParamDefinition -> listOf(MAKE_MUTABLE_QUICK_FIX)
                        is VlangFieldDefinition -> listOf()
                        is VlangConstDefinition -> listOf()
                        else                    -> return@visitQualifier
                    }

                    if (symbol is VlangMutabilityOwner && symbol.isMutable()) {
                        if (symbol.isCaptured(leftExpr)) {
                            val capture = symbol.getCapturePlace(leftExpr) ?: return@visitQualifier
                            if (!capture.isMutable) {
                                holder.registerProblem(
                                    qualifier,
                                    captureVariableMessage(kind, symbol.name),
                                    MAKE_CAPTURE_MUTABLE_QUICK_FIX,
                                )
                            }
                        }

                        return@visitQualifier
                    }

                    holder.registerProblem(
                        qualifier,
                        message(kind, symbol.name),
                        *quickFix.toTypedArray(),
                    )
                }
            }

            private fun visitQualifier(qualifier: VlangCompositeElement?, onResolved: (PsiElement, PsiElement) -> Unit) {
                if (qualifier == null) return
                val refExpr = qualifier as? VlangReferenceExpression
                val resolved = refExpr?.resolve()
                if (resolved != null) {
                    onResolved(refExpr, resolved)
                }

                val next = refExpr?.getQualifier() ?: return
                visitQualifier(next, onResolved)
            }
        }
    }

    companion object {
        val MAKE_MUTABLE_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Make mutable"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val ref = descriptor.psiElement as? VlangReferenceExpression ?: return
                val resolved = ref.resolve() as? VlangMutabilityOwner ?: return
                resolved.makeMutable()
            }
        }

        val MAKE_CAPTURE_MUTABLE_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Capture variable as mutable"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val ref = descriptor.psiElement as? VlangReferenceExpression ?: return
                val resolved = ref.resolve() as? VlangNamedElement ?: return
                val capture = resolved.getCapturePlace(ref) ?: return
                capture.makeMutable()
            }
        }
    }
}
