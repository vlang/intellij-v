package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.unused.VlangDeleteQuickFix.Companion.DELETE_INTERFACE_FIX
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.search.VlangInheritorsLineMarkerProvider
import org.vlang.lang.search.VlangInheritorsSearch

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
