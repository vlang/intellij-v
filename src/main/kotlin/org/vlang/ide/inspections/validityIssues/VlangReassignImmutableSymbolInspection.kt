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

                    visitQualifier(leftExpr) { qualifier, symbol ->
                        if (symbol is VlangVarDefinition && !symbol.isMutable()) {
                            holder.registerProblem(
                                qualifier,
                                "Immutable variable '${symbol.name}' cannot be reassigned",
                                MAKE_MUTABLE_QUICK_FIX
                            )
                        }

                        if (symbol is VlangReceiver && !symbol.isMutable()) {
                            holder.registerProblem(
                                qualifier,
                                "Immutable receiver '${symbol.name}' cannot be reassigned",
                                MAKE_MUTABLE_QUICK_FIX
                            )
                        }

                        if (symbol is VlangParamDefinition && !symbol.isMutable()) {
                            holder.registerProblem(
                                qualifier,
                                "Immutable parameter '${symbol.name}' cannot be reassigned",
                                MAKE_MUTABLE_QUICK_FIX
                            )
                        }

                        if (symbol is VlangFieldDefinition && !symbol.isMutable()) {
                            holder.registerProblem(
                                qualifier,
                                "Immutable field '${symbol.name}' cannot be reassigned",
                            )
                        }

                        if (symbol is VlangConstDefinition) {
                            holder.registerProblem(
                                qualifier,
                                "Constant '${symbol.name}' cannot be reassigned",
                            )
                        }
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
