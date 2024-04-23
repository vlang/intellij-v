package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.psi.VlangReferenceExpression

open class VlangAccessInstructionImpl(anchor: VlangReferenceExpression, override val access: ReadWriteAccessDetector.Access) :
    VlangLinearInstructionImpl(anchor), VlangAccessInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processAccessInstruction(this)
    }

    override fun toString() = super.toString() + " ACCESS (${access.toString().uppercase()}) ${anchor?.text}"
}
