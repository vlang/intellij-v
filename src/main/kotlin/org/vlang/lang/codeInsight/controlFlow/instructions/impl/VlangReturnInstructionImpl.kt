package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangReturnInstruction
import org.vlang.lang.psi.VlangReturnStatement

class VlangReturnInstructionImpl(returnStatement: VlangReturnStatement) :
    VlangLinearInstructionImpl(returnStatement), VlangReturnInstruction {

    override val results: List<PsiElement> = returnStatement.expressionList

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processReturnInstruction(this)
    }

    override fun toString() = super.toString() + " RETURN " + results.joinToString(", ") { it.text }
}
