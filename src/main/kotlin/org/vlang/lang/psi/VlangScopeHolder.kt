package org.vlang.lang.psi

import org.vlang.lang.codeInsight.controlFlow.VlangControlFlow

interface VlangScopeHolder : VlangCompositeElement {
    fun controlFlow(): VlangControlFlow
    fun scope(): VlangScope
}
