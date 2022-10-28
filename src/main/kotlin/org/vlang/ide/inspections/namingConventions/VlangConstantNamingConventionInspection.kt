package org.vlang.ide.inspections.namingConventions

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangConstDefinition
import org.vlang.lang.psi.VlangVisitor

class VlangConstantNamingConventionInspection : VlangNamingConventionInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitConstDefinition(o: VlangConstDefinition) {
                holder.checkSnakeCase(o, "Constant")
            }
        }
    }
}
