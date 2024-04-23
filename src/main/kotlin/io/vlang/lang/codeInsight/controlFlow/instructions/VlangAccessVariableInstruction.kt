package io.vlang.lang.codeInsight.controlFlow.instructions

import io.vlang.lang.psi.VlangVarDefinition

interface VlangAccessVariableInstruction : VlangAccessInstruction {
    val definition: VlangVarDefinition
}
