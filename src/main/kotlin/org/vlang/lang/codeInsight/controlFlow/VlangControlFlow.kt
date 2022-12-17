package org.vlang.lang.codeInsight.controlFlow

import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangExitPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction

interface VlangControlFlow {
    fun instructions(): List<VlangInstruction>
    fun entryPoint(): VlangEntryPointInstruction
    fun exitPoint(): VlangExitPointInstruction

    fun <T : VlangInstruction> getInstruction(anchor: PsiElement, aClass: Class<T>): T?
    fun <T : VlangInstruction> getInstructions(anchor: PsiElement, aClass: Class<T>): List<T>
}
