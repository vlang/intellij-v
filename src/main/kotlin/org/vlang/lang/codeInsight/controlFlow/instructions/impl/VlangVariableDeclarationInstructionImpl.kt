package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangVariableDeclarationInstruction
import org.vlang.lang.psi.VlangVarDefinition

class VlangVariableDeclarationInstructionImpl(
    override val definition: VlangVarDefinition,
) : VlangLinearInstructionImpl(definition), VlangVariableDeclarationInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processVariableDeclarationInstruction(this)
    }

    override fun toString() = super.toString() + " VARIABLE_DECLARATION ${definition.name}"
}
