package org.vlang.lang.codeInsight.controlFlow.instructions

interface VlangHostInstruction : VlangInstruction {
    fun successors(): MutableCollection<VlangInstruction>
}
