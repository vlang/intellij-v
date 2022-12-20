package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_ENUM_FIX
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangVisitor

class VlangUnusedEnumInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitEnumDeclaration(enum: VlangEnumDeclaration) = check(holder, enum)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean = true

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        element as VlangEnumDeclaration

        val identifier = element.getIdentifier() ?: return
        val range = identifier.textRangeInParent.shiftRight(element.enumType.textRangeInParent.startOffset)
        holder.registerProblem(
            element,
            "Unused enum '${element.name}'",
            ProblemHighlightType.LIKE_UNUSED_SYMBOL, range,
            DELETE_ENUM_FIX,
        )
    }
}
