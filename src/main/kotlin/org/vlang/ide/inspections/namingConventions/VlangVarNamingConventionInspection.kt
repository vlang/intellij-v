package org.vlang.ide.inspections.namingConventions

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangVarDefinition
import org.vlang.lang.psi.VlangVisitor

class VlangVarNamingConventionInspection : VlangNamingConventionInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitVarDefinition(o: VlangVarDefinition) {
                super.visitVarDefinition(o)
                holder.checkSnakeCase(o, "Variable")
            }
        }
    }
}
