package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import io.vlang.lang.codeInsight.controlFlow.instructions.VlangFunctionDeclarationInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.psi.VlangFunctionDeclaration

class VlangFunctionDeclarationInstructionImpl(override val declaration: VlangFunctionDeclaration) :
    VlangLinearInstructionImpl(declaration), VlangFunctionDeclarationInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processFunctionDeclarationInstruction(this)
    }

    override fun toString() = super.toString() + " FUNCTION_DECLARATION " + declaration.name
}
