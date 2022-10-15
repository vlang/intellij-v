package org.vlang.ide.inspections.validityIssues

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*

class VlangReassignImmutableSymbolInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitAssignmentStatement(o: VlangAssignmentStatement) {
                super.visitAssignmentStatement(o)

                val leftExprs = o.leftHandExprList.expressionList
                for (leftExpr in leftExprs) {
                    if (leftExpr !is VlangReferenceExpression) continue

                    val symbol = leftExpr.reference.resolve()
                    if (symbol is VlangVarDefinition && !symbol.isMutable()) {
                        holder.registerProblem(
                            leftExpr,
                            "Immutable variable '${symbol.name}' cannot be reassigned",
                            MAKE_MUTABLE_QUICK_FIX
                        )
                    }

                    if (symbol is VlangReceiver && !symbol.isMutable()) {
                        holder.registerProblem(
                            leftExpr,
                            "Immutable receiver '${symbol.name}' cannot be reassigned",
                            MAKE_MUTABLE_QUICK_FIX
                        )
                    }

                    if (symbol is VlangConstDefinition) {
                        holder.registerProblem(
                            leftExpr,
                            "Constant '${symbol.name}' cannot be reassigned",
                        )
                    }
                }
            }
        }
    }

    companion object {
        val MAKE_MUTABLE_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Make mutable"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val ref = descriptor.psiElement as? VlangReferenceExpression ?: return
                val resolved = ref.resolve() ?: return
                if (resolved is VlangVarDefinition) {
                    resolved.makeMutable()
                } else if (resolved is VlangReceiver) {
                    resolved.makeMutable()
                }
            }
        }
    }
}
