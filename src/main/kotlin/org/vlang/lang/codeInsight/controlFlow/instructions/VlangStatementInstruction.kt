package org.vlang.lang.codeInsight.controlFlow.instructions

import org.vlang.lang.psi.VlangStatement

interface VlangStatementInstruction : VlangInstruction {
    val statement: VlangStatement
}
