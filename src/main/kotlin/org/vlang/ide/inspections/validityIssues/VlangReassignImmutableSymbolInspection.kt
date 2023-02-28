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
                    if (leftExpr !is VlangReferenceExpression) continue
                    checkReferenceExpression(leftExpr) { kind, name ->
                        if (kind == "constant") "Constant '$name' cannot be reassigned"
                        else "Immutable $kind '$name' cannot be reassigned"
                    }
                }
            }

            override fun visitAppendStatement(stmt: VlangAppendStatement) {
                if (VlangUnsafeUtil.insideUnsafe(stmt)) return
                val leftExpr = stmt.left ?: return
                if (leftExpr !is VlangReferenceExpression) return
                checkReferenceExpression(leftExpr) { kind, name -> "Cannot append to immutable $kind '$name'" }
            }

            private fun checkReferenceExpression(leftExpr: VlangExpression?, message: (kind: String, name: String?) -> String) {
                visitQualifier(leftExpr) { qualifier, symbol ->
                    if (symbol is VlangVarDefinition && !symbol.isMutable()) {
                        holder.registerProblem(
                            qualifier,
                            message("variable", symbol.name),
                            MAKE_MUTABLE_QUICK_FIX,
                        )
                    }

                    if (symbol is VlangReceiver && !symbol.isMutable()) {
                        holder.registerProblem(
                            qualifier,
                            message("receiver", symbol.name),
                            MAKE_MUTABLE_QUICK_FIX,
                        )
                    }

                    if (symbol is VlangParamDefinition && !symbol.isMutable()) {
                        holder.registerProblem(
                            qualifier,
                            message("parameter", symbol.name),
                            MAKE_MUTABLE_QUICK_FIX,
                        )
                    }

                    if (symbol is VlangFieldDefinition && !symbol.isMutable()) {
                        holder.registerProblem(
                            qualifier,
                            message("field", symbol.name),
                        )
                    }

                    if (symbol is VlangConstDefinition) {
                        holder.registerProblem(
                            qualifier,
                            message("constant", symbol.name),
                        )
                    }
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
    }
}
