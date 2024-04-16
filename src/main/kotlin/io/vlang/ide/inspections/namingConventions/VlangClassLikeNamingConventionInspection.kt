package io.vlang.ide.inspections.namingConventions

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.lang.psi.VlangEnumDeclaration
import io.vlang.lang.psi.VlangInterfaceDeclaration
import io.vlang.lang.psi.VlangStructDeclaration
import io.vlang.lang.psi.VlangVisitor

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
