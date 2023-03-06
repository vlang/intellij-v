package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessParameterInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangParamDefinition
import org.vlang.lang.psi.VlangReferenceExpression

class VlangAccessParameterInstructionImpl(
    anchor: VlangReferenceExpression,
    override val definition: VlangParamDefinition,
    override val access: ReadWriteAccessDetector.Access,
) : VlangAccessInstructionImpl(anchor, access), VlangAccessParameterInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processAccessParameterInstruction(this)
    }

    override fun toString() = super.toString() + " ACCESS_PARAMETER (${access.toString().uppercase()}) ${definition.name}"
}
