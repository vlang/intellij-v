package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_STRUCT_FIX
import org.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_UNION_FIX
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.VlangVisitor

class VlangUnusedStructInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitStructDeclaration(struct: VlangStructDeclaration) = check(holder, struct)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean = true

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        element as VlangStructDeclaration

        val identifier = element.getIdentifier() ?: return
        val range = identifier.textRangeInParent.shiftRight(element.structType.textRangeInParent.startOffset)
        holder.registerProblem(
            element,
            "Unused ${element.kindName} '${element.name}'",
            ProblemHighlightType.LIKE_UNUSED_SYMBOL, range,
            if (element.isUnion) DELETE_UNION_FIX else DELETE_STRUCT_FIX,
        )
    }
}
