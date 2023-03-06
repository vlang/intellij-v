package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

abstract class VlangInstructionImpl(override val anchor: PsiElement?) : VlangInstruction {
    override var id: Int = 0

    private val predecessors = mutableListOf<VlangInstruction>()

    abstract override fun appendSuccessors(instructions: MutableCollection<VlangInstruction>)

    abstract fun replaceSuccessor(oldSuccessor: VlangInstructionImpl?, newSuccessor: VlangInstructionImpl?)

    override fun predecessors(): MutableCollection<VlangInstruction> {
        return predecessors
    }

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processInstruction(this)
    }

    open fun join(instruction: VlangInstructionImpl) {
        instruction.predecessors().add(this)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        val preds = predecessors().joinToString(", ") { it.id.toString() }.ifEmpty { "-" }
        builder.append(String.format("p:%-5s s:%-5s %2s:", preds, "-", id))
        return builder.toString()
    }
}
