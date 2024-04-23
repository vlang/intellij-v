package io.vlang.ide.inspections.unused

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.search.searches.ReferencesSearch
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.psi.VlangLabelDefinition
import io.vlang.lang.psi.VlangVisitor

class VlangUnusedLabelInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): VlangVisitor {
        return object : VlangVisitor() {
            override fun visitLabelDefinition(label: VlangLabelDefinition) {
                val search = ReferencesSearch.search(label, label.useScope)
                if (search.findFirst() != null)
                    return

                holder.registerProblem(label, "Unused label <code>#ref</code> #loc", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
            }
        }
    }
}
