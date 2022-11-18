package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangDeferInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangDeferStatement

class VlangDeferInstructionImpl(deferStatement: VlangDeferStatement) :
    VlangLinearInstructionImpl(deferStatement), VlangDeferInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processDeferInstruction(this)
    }

    override fun toString() = super.toString() + " DEFER"
}
