package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangBlock
import org.vlang.lang.psi.VlangStatement
import org.vlang.lang.psi.VlangVarDefinition

class VlangVarReference(element: VlangVarDefinition) : VlangCachedReference<VlangVarDefinition>(element) {
    private val contextBlock = element.parentOfType<VlangBlock>()

    override fun resolveInner(): PsiElement? {
        val p = VlangVarProcessor(myElement, false)
        processResolveVariants(p)
        return p.getResult()
    }

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        if (contextBlock == null) {
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

        contextBlock.processDeclarations(
            p,
            ResolveState.initial(),
            PsiTreeUtil.getParentOfType(myElement, VlangStatement::class.java),
            myElement
        )
        return true
    }
}
