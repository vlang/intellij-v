package org.vlang.lang.codeInsight.controlFlow.instructions.impl

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangStatementInstruction
import org.vlang.lang.psi.VlangStatement

class VlangStatementInstructionImpl(override val statement: VlangStatement) :
    VlangLinearInstructionImpl(statement), VlangStatementInstruction {

    override fun process(visitor: VlangInstructionProcessor): Boolean {
        return visitor.processStatementInstruction(this)
    }

    override fun toString() = super.toString() + " STATEMENT " + statement.text.lines().first()
}
