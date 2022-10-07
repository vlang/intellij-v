package org.vlang.ide.inspections.style

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*

class VlangRedundantParenthesesInspection : VlangBaseInspection() {
    // TODO: More generic approach?
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitIfExpression(expr: VlangIfExpression) {
                if (expr.expression is VlangParenthesesExpr) {
                    holder.registerProblem(expr.expression)
                }
            }

            override fun visitMatchExpression(expr: VlangMatchExpression) {
                if (expr.expression is VlangParenthesesExpr) {
                    holder.registerProblem(expr.expression)
                }
            }
        }
    }

    private fun ProblemsHolder.registerProblem(expr: VlangCompositeElement?) {
        if (expr == null) {
            return
        }

        registerProblem(
            expr, "Redundant parentheses",
            ProblemHighlightType.WARNING, UNWRAP_QUICK_FIX
        )
    }

    class UnwrapQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Unwrap parentheses"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val element = descriptor.psiElement
            if (element !is VlangParenthesesExpr || element.expression == null) return

            element.replace(element.expression!!)
        }
    }

    companion object {
        private val UNWRAP_QUICK_FIX = UnwrapQuickFix()
    }
}
