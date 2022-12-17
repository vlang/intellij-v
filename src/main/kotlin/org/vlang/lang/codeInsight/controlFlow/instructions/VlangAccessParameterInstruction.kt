package org.vlang.lang.codeInsight.controlFlow.instructions

import org.vlang.lang.psi.VlangParamDefinition

interface VlangAccessParameterInstruction : VlangAccessInstruction {
    val definition: VlangParamDefinition
}
