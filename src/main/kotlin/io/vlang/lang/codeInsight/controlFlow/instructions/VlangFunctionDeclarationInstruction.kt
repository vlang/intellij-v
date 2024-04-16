package io.vlang.lang.codeInsight.controlFlow.instructions

import io.vlang.lang.psi.VlangFunctionDeclaration

interface VlangFunctionDeclarationInstruction : VlangInstruction {
    val declaration: VlangFunctionDeclaration
}
