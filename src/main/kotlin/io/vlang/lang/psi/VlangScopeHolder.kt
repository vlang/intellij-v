package io.vlang.lang.psi

import io.vlang.lang.codeInsight.controlFlow.VlangControlFlow

interface VlangScopeHolder : VlangCompositeElement {
    fun controlFlow(): VlangControlFlow
    fun scope(): VlangScope
}
