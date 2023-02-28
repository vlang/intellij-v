package org.vlang.lang.annotator.checkers

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangResultTypeEx

class VlangOptionResultProblemsChecker(holder: AnnotationHolder) : VlangCheckerBase(holder) {
    override fun visitReferenceExpression(expr: VlangReferenceExpression) {
        val reference = expr.reference
        val resolved = reference.multiResolve(false).firstOrNull()?.element ?: return

        if (resolved is VlangConstDefinition && resolved.name == "err" && resolved.getModuleName() == VlangCodeInsightUtil.STUBS_MODULE) {
            checkErrVariableUsage(expr, holder)
        }
    }

    private fun checkErrVariableUsage(element: PsiElement, holder: AnnotationHolder) {
        val owner = VlangLangUtil.getErrVariableOwner(element) ?: return
        val typeElement = when (owner) {
            is VlangOrBlockExpr  -> owner.expression
            is VlangIfExpression -> owner.guardVarDeclaration?.expression
            else                 -> null
        } ?: return

        val type = typeElement.getType(null)
        if (type !is VlangResultTypeEx) {
            val action = if (typeElement is VlangCallExpr) "returns" else "has"
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "'err' is always 'none', since '${typeElement.text}' $action Option type")
                .create()
        }
    }
}
