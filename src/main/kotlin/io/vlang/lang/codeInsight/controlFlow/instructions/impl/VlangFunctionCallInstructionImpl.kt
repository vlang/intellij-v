package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import io.vlang.lang.codeInsight.controlFlow.instructions.VlangFunctionCallInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.psi.VlangCallExpr

class VlangFunctionCallInstructionImpl(private val call: VlangCallExpr) :
    VlangLinearInstructionImpl(call), VlangFunctionCallInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processFunctionCallInstruction(this)
    }

    override fun toString() = super.toString() + " CALL " + call.identifier?.text
}
