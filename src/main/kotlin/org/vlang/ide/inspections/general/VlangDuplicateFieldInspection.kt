package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*

class VlangDuplicateFieldInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitStructType(o: VlangStructType) {
                holder.checkFields(o.ownFieldList)
                super.visitStructType(o)
            }

            override fun visitInterfaceType(o: VlangInterfaceType) {
                holder.checkFields(o.ownFieldList)
                super.visitInterfaceType(o)
            }

            override fun visitEnumType(o: VlangEnumType) {
                holder.checkFields(o.fieldList)
                super.visitEnumType(o)
            }
        }
    }

    private fun ProblemsHolder.checkFields(fields: List<VlangNamedElement>) {
        val names = HashSet<String>()
        for (field in fields) {
            val name = field.name
            if (name != null && !names.add(name)) {
                registerProblem(field, "Duplicate field <code>#ref</code>", ProblemHighlightType.GENERIC_ERROR)
            }
        }
    }
}
