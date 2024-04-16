package io.vlang.lang.codeInsight.controlFlow.instructions

import io.vlang.lang.psi.VlangFieldDefinition

interface VlangAccessFieldInstruction : VlangAccessInstruction {
    val definition: VlangFieldDefinition
}
