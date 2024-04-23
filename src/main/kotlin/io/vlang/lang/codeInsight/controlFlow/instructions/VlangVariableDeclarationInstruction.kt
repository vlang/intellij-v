package io.vlang.lang.codeInsight.controlFlow.instructions

import io.vlang.lang.psi.VlangVarDefinition

interface VlangVariableDeclarationInstruction : VlangInstruction {
    val definition: VlangVarDefinition
}
