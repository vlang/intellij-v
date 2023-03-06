package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import org.vlang.lang.psi.*
import org.vlang.lang.utils.VlangLabelUtil

class VlangLabelReference(label: VlangLabelRef) : VlangCachedReference<VlangLabelRef>(label) {
    private val processor: VlangScopeProcessorBase = object : VlangScopeProcessorBase(myElement) {
        override fun crossOff(e: PsiElement): Boolean {
            return e !is VlangLabelDefinition || e.isBlank()
        }
    }
    
    override fun resolveInner(): PsiElement? {
        return if (!processResolveVariants(this.processor)) this.processor.getResult() else null
    }

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
       return processAllDefinitions(processor)
    }

    private fun processAllDefinitions(processor: VlangScopeProcessor): Boolean {
        val defs = VlangLabelUtil.collectContextLabels(myElement)
        for (def in defs) {
            if (!processor.execute(def, ResolveState.initial())) {
                return false
            }
        }
        return true
    }
}
