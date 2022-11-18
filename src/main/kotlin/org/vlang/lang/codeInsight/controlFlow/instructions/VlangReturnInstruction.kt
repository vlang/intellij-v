package org.vlang.lang.codeInsight.controlFlow.instructions

import com.intellij.psi.PsiElement

interface VlangReturnInstruction : VlangInstruction {
    val results: List<PsiElement>
}
