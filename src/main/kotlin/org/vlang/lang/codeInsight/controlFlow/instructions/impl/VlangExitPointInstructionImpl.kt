package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangExitPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

class VlangExitPointInstructionImpl : VlangLinearInstructionImpl(null), VlangExitPointInstruction {
    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processExitPointInstruction(this)
    }

    override fun toString() = super.toString() + " EXIT_POINT"
}
