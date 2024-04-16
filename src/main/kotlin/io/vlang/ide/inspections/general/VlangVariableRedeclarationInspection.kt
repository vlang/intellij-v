package io.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.psi.VlangParamDefinition
import io.vlang.lang.psi.VlangReceiver
import io.vlang.lang.psi.VlangVarDefinition
import io.vlang.lang.psi.VlangVisitor
import io.vlang.utils.line

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
