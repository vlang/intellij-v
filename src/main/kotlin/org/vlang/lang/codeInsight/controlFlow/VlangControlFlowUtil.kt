package org.vlang.lang.codeInsight.controlFlow

import com.intellij.openapi.progress.ProgressManager
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor

object VlangControlFlowUtil {
    fun processFlow(controlFlow: VlangControlFlow, processor: VlangInstructionProcessor): Int {
        val instructions = controlFlow.instructions()
        for (instruction in instructions) {
            ProgressManager.checkCanceled()
            if (!instruction.process(processor)) {
                return instruction.id
            }
        }
        return 0
    }
    
    fun processPredecessors(instruction: VlangInstruction, processor: VlangInstructionProcessor) {
        val pool = ArrayDeque<VlangInstruction>()
        pool.add(instruction)

        while (pool.isNotEmpty()) {
            val current = pool.first()
            val result = current.process(processor)
            if (result) {
                val predecessors = current.predecessors()
                pool.addAll(predecessors)
            }
            pool.removeFirst()
            ProgressManager.checkCanceled()
        }
    }

    fun processSuccessors(instruction: VlangInstruction, processor: VlangInstructionProcessor) {
        val pool = ArrayDeque<VlangInstruction>()
        pool.add(instruction)

        while (pool.isNotEmpty()) {
            val current = pool.first()
            val result = current.process(processor)
            if (result) {
                current.appendSuccessors(pool)
            }
            pool.removeFirst()
            ProgressManager.checkCanceled()
        }
    }
}
