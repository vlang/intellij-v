package io.vlang.lang.codeInsight.controlFlow.instructions

import io.vlang.lang.psi.VlangStatement

interface VlangStatementInstruction : VlangInstruction {
    val statement: VlangStatement
}
