package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessVariableInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.psi.VlangReferenceExpression
import io.vlang.lang.psi.VlangVarDefinition

class VlangAccessVariableInstructionImpl(
    anchor: VlangReferenceExpression,
    override val definition: VlangVarDefinition,
    override val access: ReadWriteAccessDetector.Access,
) : VlangAccessInstructionImpl(anchor, access), VlangAccessVariableInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processAccessVariableInstruction(this)
    }

    override fun toString() = super.toString() + " ACCESS_VARIABLE (${access.toString().uppercase()}) ${definition.name}"
}
