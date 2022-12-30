package org.vlang.ide.inspections.namingConventions

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.VlangVisitor

class VlangClassLikeNamingConventionInspection : VlangNamingConventionInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitStructDeclaration(o: VlangStructDeclaration) {
                holder.checkName(o, o.kindName.replaceFirstChar { it.uppercaseChar() })
            }

            override fun visitInterfaceDeclaration(o: VlangInterfaceDeclaration) {
                holder.checkName(o, "Interface")
            }

            override fun visitEnumDeclaration(o: VlangEnumDeclaration) {
                holder.checkName(o, "Enum")
            }
        }
    }
}
