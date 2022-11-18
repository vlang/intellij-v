package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangFunctionDeclarationInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangFunctionDeclaration

class VlangFunctionDeclarationInstructionImpl(override val declaration: VlangFunctionDeclaration) :
    VlangLinearInstructionImpl(declaration), VlangFunctionDeclarationInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processFunctionDeclarationInstruction(this)
    }

    override fun toString() = super.toString() + " FUNCTION_DECLARATION " + declaration.name
}
