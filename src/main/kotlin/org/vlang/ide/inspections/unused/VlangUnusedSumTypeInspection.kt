package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.VlangVisitor

class VlangUnusedSumTypeInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitTypeAliasDeclaration(alias: VlangTypeAliasDeclaration) = check(holder, alias)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean =
        element is VlangTypeAliasDeclaration && element.aliasType != null && !element.aliasType!!.isAlias

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        val identifier = element.getIdentifier() ?: return
        holder.registerProblem(identifier, "Unused sum type '${element.name}'", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
    }
}
