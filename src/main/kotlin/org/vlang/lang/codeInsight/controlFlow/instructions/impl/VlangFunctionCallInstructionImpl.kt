package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangFunctionCallInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangCallExpr
import org.vlang.lang.psi.VlangFunctionDeclaration

class VlangFunctionCallInstructionImpl(call: VlangCallExpr, override val function: VlangFunctionDeclaration) :
    VlangLinearInstructionImpl(call), VlangFunctionCallInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processFunctionCallInstruction(this)
    }

    override fun toString() = super.toString() + " FUNCTION_CALL " + function.name
}
