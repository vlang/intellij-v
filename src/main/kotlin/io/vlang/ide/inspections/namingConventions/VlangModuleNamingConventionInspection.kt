package io.vlang.ide.inspections.namingConventions

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.lang.psi.VlangModuleClause
import io.vlang.lang.psi.VlangVisitor

class VlangModuleNamingConventionInspection : VlangNamingConventionInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitModuleClause(o: VlangModuleClause) {
                holder.checkSnakeCase(o, "Module")
            }
        }
    }
}
