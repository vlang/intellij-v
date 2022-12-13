package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangModule
import org.vlang.lang.psi.impl.VlangPomTargetPsiElement
import org.vlang.lang.psi.impl.imports.VlangModuleReference
import org.vlang.lang.psi.types.VlangAnyTypeEx
import org.vlang.lang.psi.types.VlangUnknownTypeEx

class VlangCannotInferTypeInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitTypeOwner(o: VlangTypeOwner) {
                super.visitTypeOwner(o)

                if (o.parent.parent is VlangSimpleStatement) {
                    return
                }

                if (o is VlangMutExpression) {
                    return
                }

                if (o is VlangReferenceExpression) {
                    val resolved = o.resolve()
                    if (resolved is VlangPomTargetPsiElement) {
                        return
                    }
                }

                val type = o.getType(null)
                if (type == null || type is VlangAnyTypeEx || type is VlangUnknownTypeEx) {
                    holder.registerProblem(
                        o,
                        "Cannot infer type of '${o.text}'",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }
}
