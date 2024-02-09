package org.vlang.ide.inspections.general

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.parentOfType
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.impl.VlangElementFactory

class VlangMissingFunctionNameInDocInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitComment(comment: PsiComment) {
                if (comment !is VlangDocComment) return
                val owner = comment.owner
                if (owner !is VlangFunctionDeclaration && owner !is VlangMethodDeclaration) return

                val identifier = owner.getIdentifier() ?: return
                if (!comment.isValidDoc()) {
                    holder.registerProblem(
                        identifier,
                        "Missing function name in doc comment",
                        ProblemHighlightType.WEAK_WARNING,
                        ADD_NAME_QUICK_FIX,
                    )
                }
            }
        }
    }

    companion object {
        val ADD_NAME_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Add missing name"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val decl = descriptor.psiElement.parentOfType<VlangNamedElement>() ?: return
                val comment = decl.getDocumentation() ?: return
                val owner = comment.owner ?: return
                val name = owner.name
                val newCommentText = if (comment.text.length > 2) {
                    comment.text.replaceRange(0, 3, "// $name ")
                } else {
                    "// $name"
                }
                val newComment = VlangElementFactory.createDocComment(project, newCommentText)
                comment.replace(newComment)
            }
        }
    }
}
