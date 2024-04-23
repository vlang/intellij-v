package io.vlang.lang.psi.impl

import io.vlang.lang.codeInsight.controlFlow.VlangControlFlow
import io.vlang.lang.codeInsight.controlFlow.VlangControlFlowBuilder
import io.vlang.lang.psi.VlangScope
import io.vlang.lang.psi.VlangScopeHolder

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
