package org.vlang.lang.codeInsight.controlFlow

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangExitPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction

class VlangControlFlowImpl(private val instructions: List<VlangInstruction>) : VlangControlFlow {
    override fun instructions() = instructions

    override fun entryPoint() = instructions.first() as VlangEntryPointInstruction

    override fun exitPoint() = instructions.last() as VlangExitPointInstruction

    override fun toString(): String {
        return instructions.joinToString(separator = "\n")
    }
}
