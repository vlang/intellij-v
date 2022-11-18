package org.vlang.lang.codeInsight.controlFlow.instructions

import com.intellij.psi.PsiElement

interface VlangConditionInstruction : VlangInstruction {
    val condition: PsiElement?
    val result: Boolean
}
