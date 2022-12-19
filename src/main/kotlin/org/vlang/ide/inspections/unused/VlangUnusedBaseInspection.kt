package org.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.search.searches.ReferencesSearch
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangUnusedBaseInspection : VlangBaseInspection() {
    protected abstract fun shouldBeCheck(element: VlangNamedElement): Boolean

    protected abstract fun registerProblem(holder: ProblemsHolder, element: VlangNamedElement)

    protected fun check(holder: ProblemsHolder, element: VlangNamedElement) {
        if (element.isBlank()) return
        val containingFile = element.containingFile as VlangFile
        // for now
        if (containingFile.isPlatformSpecificFile()) return
        if (element.name == null || element.name!!.startsWith("C.")) return

        if (!shouldBeCheck(element)) return
        if (isUnused(element)) {
            registerProblem(holder, element)
        }
    }

    private fun isUnused(func: VlangNamedElement): Boolean {
        return ReferencesSearch.search(func).findFirst() == null
    }
}
