package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangModule
import org.vlang.lang.psi.types.VlangAnyTypeEx
import org.vlang.lang.psi.types.VlangUnknownTypeEx
import org.vlang.utils.inside

class VlangCannotInferTypeInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitTypeOwner(o: VlangTypeOwner) {
                super.visitTypeOwner(o)

                if (o.textMatches("_")) {
                    return
                }

                if (o.parent.parent is VlangSimpleStatement) {
                    return
                }

                if (o is VlangMutExpression) {
                    return
                }

                if (o is VlangIfExpression || o is VlangCompileTimeIfExpression) {
                    return
                }

                val parentCompileTimeIf = o.parentOfType<VlangCompileTimeIfExpression>()
                if (PsiTreeUtil.isAncestor(parentCompileTimeIf?.expression, o, false)) {
                    return
                }

                if (o.inside<VlangAttribute>()) {
                    return
                }

                if (o is VlangArrayCreation) {
                    return
                }

                val text = o.text
                if (text.startsWith("C.")) {
                    return
                }

                if (o is VlangReferenceExpression) {
                    val resolved = o.resolve()
                    if (resolved is VlangImportAlias || resolved is VlangModule.VlangPomTargetPsiElement) {
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
