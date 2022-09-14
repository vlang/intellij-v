package org.vlang.ide.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangVarDefinition
import org.vlang.lang.psi.VlangVisitor

class VlangVariableRedeclarationInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {

            override fun visitVarDefinition(def: VlangVarDefinition) {
                val res = def.reference?.resolve()
                if (res != null) {
                    holder.registerProblem(def, "Variable ${def.name} is already defined")
                }
            }
        }
    }
}