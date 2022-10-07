package org.vlang.ide.inspections.namingConventions

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.VlangVisitor

class VlangTypeAliasNamingConventionInspection : VlangNamingConventionInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitTypeAliasDeclaration(o: VlangTypeAliasDeclaration) {
                holder.checkName(o, "Type alias", checkLen = false)
            }
        }
    }
}
