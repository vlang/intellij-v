package org.vlang.ide.inspections.unsafe

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangLiteral
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.utils.VlangUnsafeUtil

class VlangNilOutsideUnsafeInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitLiteral(literal: VlangLiteral) {
                if (literal.nil == null) return
                if (VlangUnsafeUtil.insideUnsafe(literal)) return

                holder.registerProblem(
                    literal, "'nil' must be used only inside unsafe block",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                )
            }
        }
    }
}
