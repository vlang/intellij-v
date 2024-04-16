package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import io.vlang.lang.codeInsight.controlFlow.instructions.VlangExitPointInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

class VlangExitPointInstructionImpl : VlangLinearInstructionImpl(null), VlangExitPointInstruction {
    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processExitPointInstruction(this)
    }

    override fun toString() = super.toString() + " EXIT_POINT"
}
