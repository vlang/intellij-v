package org.vlang.lang.codeInsight.controlFlow.instructions

import org.vlang.lang.psi.VlangFieldDefinition

interface VlangAccessFieldInstruction : VlangAccessInstruction {
    val definition: VlangFieldDefinition
}
