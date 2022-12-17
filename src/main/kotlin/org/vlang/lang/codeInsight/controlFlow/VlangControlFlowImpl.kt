package org.vlang.lang.codeInsight.controlFlow

import com.google.common.collect.ImmutableMultimap
import com.intellij.psi.PsiElement
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangExitPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction

class VlangControlFlowImpl(private val instructions: List<VlangInstruction>) : VlangControlFlow {
    private var instructionMap: ImmutableMultimap<PsiElement, VlangInstruction>? = null

    override fun instructions() = instructions

    override fun entryPoint() = instructions.first() as VlangEntryPointInstruction

    override fun exitPoint() = instructions.last() as VlangExitPointInstruction

    override fun <T : VlangInstruction> getInstruction(anchor: PsiElement, aClass: Class<T>): T? {
        return getInstructions(anchor, aClass).firstOrNull()
    }

    override fun <T : VlangInstruction> getInstructions(anchor: PsiElement, aClass: Class<T>): List<T> {
        if (instructionMap == null) {
            val map: ImmutableMultimap.Builder<PsiElement, VlangInstruction> = ImmutableMultimap.builder()
            for (i in instructions) {
                val curAnchor = i.anchor
                if (curAnchor != null) {
                    map.put(curAnchor, i)
                }
            }
            instructionMap = map.build()
        }
        return instructionMap!!.get(anchor).filterIsInstance(aClass)
    }

    override fun toString(): String {
        return instructions.joinToString(separator = "\n")
    }
}
