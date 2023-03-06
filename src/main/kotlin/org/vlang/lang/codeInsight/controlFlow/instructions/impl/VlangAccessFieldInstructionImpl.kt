package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessFieldInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangReferenceExpression

class VlangAccessFieldInstructionImpl(
    anchor: VlangReferenceExpression,
    override val definition: VlangFieldDefinition,
    override val access: ReadWriteAccessDetector.Access,
) : VlangAccessInstructionImpl(anchor, access), VlangAccessFieldInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processAccessFieldInstruction(this)
    }

    override fun toString() = super.toString() + " ACCESS_FIELD (${access.toString().uppercase()}) ${anchor?.text}"
}
