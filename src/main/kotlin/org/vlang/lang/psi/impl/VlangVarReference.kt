package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangBlock
import org.vlang.lang.psi.VlangFunctionLit
import org.vlang.lang.psi.VlangStatement
import org.vlang.lang.psi.VlangVarDefinition

class VlangVarReference(element: VlangVarDefinition) : VlangCachedReference<VlangVarDefinition>(element) {
    private val contextBlock = element.parentOfType<VlangBlock>() ?: element.containingFile

    override fun resolveInner(): PsiElement? {
        val p = VlangVarProcessor(myElement, false)
        processResolveVariants(p)
        return p.getResult()
    }

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        if (contextBlock == null || element.isBlank()) {
            return false
        }

        val p =
            if (processor is VlangVarProcessor)
                processor
            else
                object : VlangVarProcessor(myElement, processor.isCompletion()) {
                    override fun execute(e: PsiElement, state: ResolveState): Boolean {
                        return super.execute(e, state) && processor.execute(e, state)
                    }
                }

        if (!contextBlock.processDeclarations(
            p,
            ResolveState.initial(),
            PsiTreeUtil.getParentOfType(myElement, VlangStatement::class.java),
            myElement
        )) return false

        if (!processCaptureList(p)) return false

        return ResolveUtil.treeWalkUp(myElement, p) { it is VlangFunctionLit }
    }

    private fun processCaptureList(processor: VlangVarProcessor): Boolean {
        val functionLit = myElement.parentOfType<VlangFunctionLit>() ?: return true
        val captureList = functionLit.captureList?.captureList ?: return true

        for (capture in captureList) {
            if (capture.text != myElement.name) continue

            val resolved = capture.referenceExpression.reference.resolve()
            if (resolved != null) {
                return processor.execute(resolved, ResolveState.initial())
            }
        }

        return true
    }
}
