package io.vlang.lang.codeInsight.controlFlow.instructions

import io.vlang.lang.psi.VlangParamDefinition

interface VlangAccessParameterInstruction : VlangAccessInstruction {
    val definition: VlangParamDefinition
}
