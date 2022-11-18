package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

abstract class VlangLinearInstructionImpl(anchor: PsiElement?) : VlangInstructionImpl(anchor) {
    protected var successor: VlangInstructionImpl? = null

    override fun appendSuccessors(instructions: MutableCollection<VlangInstruction>) {
        ProgressIndicatorProvider.checkCanceled()
        if (successor != null) {
            instructions.add(successor!!)
        }
    }

    override fun replaceSuccessor(oldSuccessor: VlangInstructionImpl?, newSuccessor: VlangInstructionImpl?) {
        if (successor == oldSuccessor) {
            successor = newSuccessor
        }
    }

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processInstruction(this)
    }

    override fun join(instruction: VlangInstructionImpl) {
        super.join(instruction)
        successor = instruction
    }
}
