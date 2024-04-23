package io.vlang.ide.inspections.unsafe

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.psi.VlangGotoStatement
import io.vlang.lang.psi.VlangVisitor
import io.vlang.lang.utils.VlangUnsafeUtil

class VlangLabelOutsideUnsafeInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitGotoStatement(goto: VlangGotoStatement) {
                if (VlangUnsafeUtil.insideUnsafe(goto)) return

                holder.registerProblem(
                    goto, "'go to' statement should be inside unsafe block",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                )
            }
        }
    }
}
