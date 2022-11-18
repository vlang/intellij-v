package org.vlang.lang.codeInsight.controlFlow.instructions

interface VlangInstruction {
    var id: Int

    fun appendSuccessors(instructions: MutableCollection<VlangInstruction>)
    fun predecessors(): MutableCollection<VlangInstruction>
    fun process(visitor: VlangInstructionProcessor): Boolean
}
