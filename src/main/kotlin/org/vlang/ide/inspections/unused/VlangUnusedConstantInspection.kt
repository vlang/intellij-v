package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangConstDefinition
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangVisitor

class VlangUnusedConstantInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitConstDefinition(const: VlangConstDefinition) = check(holder, const)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean = true

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        val identifier = element.getIdentifier() ?: return
        holder.registerProblem(identifier, "Unused constant '${element.name}'", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
    }
}
