package io.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_INTERFACE_FIX
import io.vlang.lang.psi.VlangInterfaceDeclaration
import io.vlang.lang.psi.VlangNamedElement
import io.vlang.lang.psi.VlangVisitor
import io.vlang.lang.search.VlangInheritorsLineMarkerProvider
import io.vlang.lang.search.VlangInheritorsSearch

class VlangUnusedInterfaceInspection : VlangUnusedBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitInterfaceDeclaration(iface: VlangInterfaceDeclaration) = check(holder, iface)
        }
    }

    override fun shouldBeCheck(element: VlangNamedElement): Boolean {
        return !VlangInheritorsLineMarkerProvider.hasImplementations(element, VlangInheritorsSearch)
    }

    override fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement) {
        element as VlangInterfaceDeclaration

        val identifier = element.getIdentifier() ?: return
        val range = identifier.textRangeInParent.shiftRight(element.interfaceType.textRangeInParent.startOffset)
        holder.registerProblem(
            element,
            "Unused interface '${element.name}'",
            ProblemHighlightType.LIKE_UNUSED_SYMBOL, range,
            DELETE_INTERFACE_FIX,
        )
    }
}
