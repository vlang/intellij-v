package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.psi.VlangVisitor

class VlangUnresolvedImportInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitImportName(importName: VlangImportName) {
                val resolved = importName.resolve()
                if (resolved.isNotEmpty()) {
                    return
                }

                val name = importName.text
                holder.registerProblem(
                    importName,
                    "Unresolved import '$name'",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                )
            }
        }
    }
}
