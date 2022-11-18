package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangHostInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

class VlangHostInstructionImpl(anchor: PsiElement) : VlangInstructionImpl(anchor), VlangHostInstruction {
    protected var successors = mutableListOf<VlangInstruction>()

    override fun join(instruction: VlangInstructionImpl) {
        super.join(instruction)
        successors.add(instruction)
    }

    override fun appendSuccessors(instructions: MutableCollection<VlangInstruction>) {
        ProgressIndicatorProvider.checkCanceled()
        instructions.addAll(successors)
    }

    override fun replaceSuccessor(oldSuccessor: VlangInstructionImpl?, newSuccessor: VlangInstructionImpl?) {
        val newSuccessors = mutableListOf<VlangInstruction>()
        for (successor in successors) {
            if (successor == oldSuccessor) {
                if (newSuccessor != null) {
                    newSuccessors.add(newSuccessor)
                }
            } else {
                newSuccessors.add(successor)
            }
        }
        successors = newSuccessors
    }

    override fun successors() = successors

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processHostInstruction(this)
    }

    override fun toString() = super.toString() + " HOST"
}
