package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangVariableDeclarationInstruction
import io.vlang.lang.psi.VlangVarDefinition

class VlangVariableDeclarationInstructionImpl(
    override val definition: VlangVarDefinition,
) : VlangLinearInstructionImpl(definition), VlangVariableDeclarationInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processVariableDeclarationInstruction(this)
    }

    override fun toString() = super.toString() + " VARIABLE_DECLARATION ${definition.name}"
}
