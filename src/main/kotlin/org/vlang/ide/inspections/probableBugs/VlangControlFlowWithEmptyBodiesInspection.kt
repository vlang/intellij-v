package org.vlang.ide.inspections.probableBugs

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.findTopmostParentInFile
import com.intellij.psi.util.parentOfType
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*

class VlangControlFlowWithEmptyBodiesInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitIfExpression(expr: VlangIfExpression) {
                super.visitIfExpression(expr)
                val ifExpr = expr.findTopmostParentInFile(true) { el -> el is VlangIfExpression } ?: return
                val isStatement = ifExpr.parent is VlangLeftHandExprList && ifExpr.parent.parent is VlangStatement
                if (!isStatement) {
                    return
                }

                if (expr.elseBranch != null) {
                    return
                }

                val ifBlock = expr.block ?: return
                if (ifBlock.statementList.isEmpty() && ifBlock.childrenOfType<PsiComment>().isEmpty()) {
                    // todo: remove if and negate the else condition, then simplify the statement
                    holder.registerProblem(
                        expr.`if`, "'if' has empty body",
                        ProblemHighlightType.WARNING, EmptyControlFlowQuickFix()
                    )
                }
            }

            override fun visitElseBranch(elseBranch: VlangElseBranch) {
                super.visitElseBranch(elseBranch)

                val parentIf = elseBranch.findTopmostParentInFile(false) { el -> el is VlangIfExpression } ?: return
                val isStatement = parentIf.parent is VlangLeftHandExprList && parentIf.parent.parent is VlangStatement
                if (!isStatement) {
                    return
                }

                if (elseBranch.ifExpression != null) {
                    return
                }

                val elseBlock = elseBranch.block ?: return
                if (elseBlock.statementList.isEmpty() && elseBlock.childrenOfType<PsiComment>().isEmpty()) {
                    holder.registerProblem(
                        elseBranch.`else`, "'else' has empty body",
                        ProblemHighlightType.WARNING, EmptyControlFlowQuickFix()
                    )
                }
            }

            override fun visitForStatement(o: VlangForStatement) {
                super.visitForStatement(o)

                val forBlock = o.block ?: return
                if (forBlock.statementList.isEmpty() && forBlock.childrenOfType<PsiComment>().isEmpty()) {
                    holder.registerProblem(
                        o.`for`, "'for' has empty body",
                        ProblemHighlightType.WARNING, EmptyControlFlowQuickFix()
                    )
                }
            }
        }
    }

    class EmptyControlFlowQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Remove empty statement"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val element = descriptor.psiElement ?: return
            val elementToDelete = when (element.text) {
                "if" -> element.parentOfType<VlangElseBranch>() ?: element.parentOfType<VlangIfExpression>()
                "else" -> element.parentOfType<VlangElseBranch>()
                "for" -> element.parentOfType<VlangForStatement>()
                else -> null
            } ?: return

            elementToDelete.delete()
        }
    }
}
