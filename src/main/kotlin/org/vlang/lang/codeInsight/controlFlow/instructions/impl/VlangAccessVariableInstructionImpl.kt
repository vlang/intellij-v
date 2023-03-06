package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessVariableInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.VlangVarDefinition

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
