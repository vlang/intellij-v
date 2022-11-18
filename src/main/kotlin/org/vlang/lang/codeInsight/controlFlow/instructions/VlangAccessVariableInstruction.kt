package org.vlang.lang.codeInsight.controlFlow.instructions

import org.vlang.lang.psi.VlangVarDefinition

interface VlangAccessVariableInstruction : VlangAccessInstruction {
    val definition: VlangVarDefinition
}
