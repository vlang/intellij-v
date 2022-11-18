package org.vlang.lang.psi

import org.vlang.lang.codeInsight.controlFlow.VlangControlFlow

interface VlangScope {
    fun controlFlow(): VlangControlFlow
    fun clear()
}
