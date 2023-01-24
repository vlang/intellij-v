package org.vlang.ide.inspections.probableBugs

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangElseBranch
import org.vlang.lang.psi.VlangForStatement
import org.vlang.lang.psi.VlangIfExpression
import org.vlang.lang.psi.VlangVisitor

class VlangControlFlowWithEmptyBodiesInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitIfExpression(o: VlangIfExpression) {
                super.visitIfExpression(o)

                if (o.elseBranch != null) {
                    return
                }

                val ifBlock = o.block ?: return
                if (ifBlock.statementList.isEmpty() && ifBlock.childrenOfType<PsiComment>().isEmpty()) {
                    // todo: remove if and negate the else condition, then simplify the statement
                    holder.registerProblem(
                        o.`if`, "'if' has empty body",
                        ProblemHighlightType.WARNING, EmptyControlFlowQuickFix()
                    )
                }
            }

            override fun visitElseBranch(o: VlangElseBranch) {
                super.visitElseBranch(o)

                if (o.ifExpression != null) {
                    return
                }

                val elseBlock = o.block ?: return
                if (elseBlock.statementList.isEmpty() && elseBlock.childrenOfType<PsiComment>().isEmpty()) {
                    holder.registerProblem(
                        o.`else`, "'else' has empty body",
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
