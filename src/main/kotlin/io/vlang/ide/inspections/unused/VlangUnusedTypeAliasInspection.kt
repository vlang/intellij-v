package io.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_TYPE_ALIAS_FIX
import io.vlang.lang.psi.VlangNamedElement
import io.vlang.lang.psi.VlangTypeAliasDeclaration
import io.vlang.lang.psi.VlangVisitor

class VlangUnusedTypeAliasInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitTypeAliasDeclaration(alias: VlangTypeAliasDeclaration) = check(holder, alias)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean =
        element is VlangTypeAliasDeclaration && element.aliasType != null && element.aliasType!!.isAlias

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        element as VlangTypeAliasDeclaration

        val identifier = element.getIdentifier() ?: return
        val aliasType = element.aliasType ?: return
        val range = identifier.textRangeInParent.shiftRight(aliasType.textRangeInParent.startOffset)
        holder.registerProblem(
            element,
            "Unused type alias '${element.name}'",
            ProblemHighlightType.LIKE_UNUSED_SYMBOL, range,
            DELETE_TYPE_ALIAS_FIX,
        )
    }
}
