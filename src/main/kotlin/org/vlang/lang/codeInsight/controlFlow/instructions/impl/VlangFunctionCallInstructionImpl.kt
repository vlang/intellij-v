package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangFunctionCallInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangCallExpr

class VlangFunctionCallInstructionImpl(private val call: VlangCallExpr) :
    VlangLinearInstructionImpl(call), VlangFunctionCallInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processFunctionCallInstruction(this)
    }

    override fun toString() = super.toString() + " CALL " + call.identifier?.text
}
