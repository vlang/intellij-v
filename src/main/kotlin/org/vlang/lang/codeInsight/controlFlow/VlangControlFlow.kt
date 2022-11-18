package org.vlang.lang.codeInsight.controlFlow

import org.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangExitPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction

interface VlangControlFlow {
    fun instructions(): List<VlangInstruction>
    fun entryPoint(): VlangEntryPointInstruction
    fun exitPoint(): VlangExitPointInstruction
}
