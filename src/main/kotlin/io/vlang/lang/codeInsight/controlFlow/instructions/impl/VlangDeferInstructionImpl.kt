package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import io.vlang.lang.codeInsight.controlFlow.instructions.VlangDeferInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.psi.VlangDeferStatement

class VlangDeferInstructionImpl(deferStatement: VlangDeferStatement) :
    VlangLinearInstructionImpl(deferStatement), VlangDeferInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processDeferInstruction(this)
    }

    override fun toString() = super.toString() + " DEFER"
}
