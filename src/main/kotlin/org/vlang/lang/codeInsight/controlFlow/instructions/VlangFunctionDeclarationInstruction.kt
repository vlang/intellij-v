package org.vlang.lang.codeInsight.controlFlow.instructions

import org.vlang.lang.psi.VlangFunctionDeclaration

interface VlangFunctionDeclarationInstruction : VlangInstruction {
    val declaration: VlangFunctionDeclaration
}
