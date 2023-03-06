package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangParamDefinition
import org.vlang.lang.psi.VlangReceiver
import org.vlang.lang.psi.VlangVarDefinition
import org.vlang.lang.psi.VlangVisitor
import org.vlang.utils.line

class VlangVariableRedeclarationInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitVarDefinition(def: VlangVarDefinition) {
                val res = def.reference.resolve()
                if (res != null) {
                    val kind = if (res is VlangParamDefinition) "as parameter " else if (res is VlangReceiver) "as receiver " else ""
                    holder.registerProblem(
                        def,
                        "Variable '${def.name}' is already defined ${kind}on line ${res.line()}",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }
}
