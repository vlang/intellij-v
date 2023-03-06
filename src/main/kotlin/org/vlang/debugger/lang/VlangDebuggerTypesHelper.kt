package org.vlang.debugger.lang

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveState
import com.intellij.xdebugger.XSourcePosition
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrDebuggerTypesHelper
import com.jetbrains.cidr.execution.debugger.evaluation.CidrMemberValue
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.utils.ancestorOrSelf

class VlangDebuggerTypesHelper(process: CidrDebugProcess) : CidrDebuggerTypesHelper(process) {
    override fun createReferenceFromText(`var`: LLValue, context: PsiElement): PsiReference? = null

    override fun computeSourcePosition(value: CidrMemberValue): XSourcePosition? = null

    override fun isImplicitContextVariable(position: XSourcePosition, `var`: LLValue): Boolean = false

    override fun resolveProperty(value: CidrMemberValue, dynamicTypeName: String?): XSourcePosition? = null

    override fun resolveToDeclaration(position: XSourcePosition?, value: LLValue): PsiElement? {
        val context = getContextElement(position)
        return resolveToDeclaration(context, value.name)
    }
}

private fun resolveToDeclaration(ctx: PsiElement?, name: String): PsiElement? {
    val function = ctx?.ancestorOrSelf<VlangFunctionOrMethodDeclaration>() ?: return null

    var foundElement = ctx
    function.processDeclarations({ element, _ ->
        if (element is VlangNamedElement && element.name == name) {
            foundElement = element
            return@processDeclarations false
        }
        true
    }, ResolveState.initial(), null, ctx)

   return foundElement
}
