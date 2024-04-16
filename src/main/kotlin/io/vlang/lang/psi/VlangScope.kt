package io.vlang.lang.psi

import io.vlang.lang.codeInsight.controlFlow.VlangControlFlow

interface VlangScope {
    fun controlFlow(): VlangControlFlow
    fun clear()
}
