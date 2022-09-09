package org.vlang.ide.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.*

class VlangUnresolvedLabelInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitLabelRef(labelRef: VlangLabelRef) {
                val label = labelRef.reference.resolve()
                if (label == null) {
                    holder.registerProblem(labelRef, "Unresolved label '${labelRef.name}'", ProblemHighlightType.GENERIC_ERROR)
                }
            }
        }
    }
}
