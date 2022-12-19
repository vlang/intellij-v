package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
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
        val identifier = element.getIdentifier() ?: return
        val kind = if (element is VlangStructDeclaration && element.isUnion) "union" else "struct"
        holder.registerProblem(identifier, "Unused kind '${element.name}'", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
    }
}
