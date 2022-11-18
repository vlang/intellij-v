package org.vlang.lang.codeInsight.controlFlow.instructions

import org.vlang.lang.psi.VlangVarDefinition

interface VlangVariableDeclarationInstruction : VlangInstruction {
    val definition: VlangVarDefinition
}
