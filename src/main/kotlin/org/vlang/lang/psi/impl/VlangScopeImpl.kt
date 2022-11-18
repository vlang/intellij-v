package org.vlang.lang.psi.impl

import org.vlang.lang.codeInsight.controlFlow.VlangControlFlow
import org.vlang.lang.codeInsight.controlFlow.VlangControlFlowBuilder
import org.vlang.lang.psi.VlangScope
import org.vlang.lang.psi.VlangScopeHolder

class VlangScopeImpl(private val scopeHolder: VlangScopeHolder) : VlangScope {
    private var controlFlow: VlangControlFlow? = null

    override fun controlFlow(): VlangControlFlow {
        if (controlFlow == null) {
            controlFlow = VlangControlFlowBuilder(scopeHolder).build()
        }

        return controlFlow!!
    }

    override fun clear() {
        controlFlow = null
    }
}
