package org.vlang.lang.codeInsight.controlFlow.instructions

import com.intellij.psi.PsiElement

interface VlangInstruction {
    var id: Int
    val anchor: PsiElement?

    fun appendSuccessors(instructions: MutableCollection<VlangInstruction>)
    fun predecessors(): MutableCollection<VlangInstruction>
    fun process(visitor: VlangInstructionProcessor): Boolean
}
