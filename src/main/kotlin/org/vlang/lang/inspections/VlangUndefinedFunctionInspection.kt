package org.vlang.lang.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.VlangVisitor

class VlangUndefinedFunctionInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitReferenceExpression(o: VlangReferenceExpression) {
                val ref = o.reference
                val func = ref.resolve()
                if (func == null) {
                    holder.registerProblem(o.getIdentifier(), "Undefined function", ProblemHighlightType.GENERIC_ERROR)
                }
            }
        }
    }
}
