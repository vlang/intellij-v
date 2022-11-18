package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

class VlangEntryPointInstructionImpl(anchor: PsiElement) : VlangLinearInstructionImpl(anchor), VlangEntryPointInstruction {
    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processEntryPointInstruction(this)
    }

    override fun toString() = super.toString() + " ENTRY_POINT"
}
