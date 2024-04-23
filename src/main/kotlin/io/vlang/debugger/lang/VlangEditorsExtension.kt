package io.vlang.debugger.lang

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.jetbrains.cidr.execution.debugger.CidrDebuggerEditorsExtensionBase
import io.vlang.lang.psi.VlangCompositeElement
import io.vlang.lang.psi.VlangDebuggerExpressionCodeFragment
import io.vlang.lang.psi.VlangStatement
import io.vlang.utils.ancestorOrSelf

class VlangEditorsExtension : CidrDebuggerEditorsExtensionBase() {
    override fun getContext(project: Project, sourcePosition: XSourcePosition): PsiElement? =
        super.getContext(project, sourcePosition)?.ancestorOrSelf<VlangStatement>()

    override fun createExpressionCodeFragment(project: Project, text: String, context: PsiElement, mode: EvaluationMode): PsiFile =
        if (context is VlangCompositeElement) {
            VlangDebuggerExpressionCodeFragment(project, text, context)
        } else {
            super.createExpressionCodeFragment(project, text, context, mode)
        }
}
