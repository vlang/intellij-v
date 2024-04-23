package io.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.psi.PsiElement
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangConditionInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionWithInversed

class VlangConditionInstructionImpl(override val condition: PsiElement?, override val result: Boolean) :
    VlangLinearInstructionImpl(condition), VlangConditionInstruction, VlangInstructionWithInversed {

    override lateinit var inversed: VlangInstructionWithInversed

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processConditionInstruction(this)
    }

    override fun toString() = super.toString() + " CONDITION($result, $condition) "
}
