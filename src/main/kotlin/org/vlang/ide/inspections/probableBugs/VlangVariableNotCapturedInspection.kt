package org.vlang.ide.inspections.probableBugs

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.parentOfType
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*

class VlangVariableNotCapturedInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitReferenceExpression(o: VlangReferenceExpression) {
                super.visitReferenceExpression(o)

                val resolved = o.reference.resolve() ?: return
                if (resolved !is VlangVarDefinition && resolved !is VlangParamDefinition && resolved !is VlangReceiver) return
                resolved as VlangNamedElement
                if (resolved.name == null) return

                val outerLambda = o.parentOfType<VlangFunctionLit>()
                val definedInLambda = resolved.parentOfType<VlangFunctionLit>()
                if (definedInLambda == outerLambda) return

                val functionLit = o.parentOfType<VlangFunctionLit>() ?: return
                val captureList = functionLit.captureList?.captureList ?: emptyList()
                if (captureList.any { it.referenceExpression.text == resolved.name }) return

                val kind = when (resolved) {
                    is VlangVarDefinition   -> "variable"
                    is VlangParamDefinition -> "parameter"
                    is VlangReceiver        -> "receiver"
                    else                    -> "variable"
                }
                val kindTitle = kind.replaceFirstChar { it.titlecase() }

                holder.registerProblem(
                    o, "$kindTitle '${resolved.name}' is not captured",
                    ProblemHighlightType.GENERIC_ERROR, CaptureVariableQuickFix(kind)
                )
            }
        }
    }

    class CaptureVariableQuickFix(private val kind: String) : LocalQuickFix {
        override fun getFamilyName() = "Add $kind to capture list"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val element = descriptor.psiElement ?: return
            val resolved = element.reference?.resolve() as? VlangNamedElement ?: return
            val functionLit = element.parentOfType<VlangFunctionLit>() ?: return
            val name = resolved.name ?: return

            functionLit.addCapture(name)
        }
    }
}
