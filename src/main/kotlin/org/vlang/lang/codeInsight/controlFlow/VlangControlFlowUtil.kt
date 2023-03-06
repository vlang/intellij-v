package org.vlang.lang.codeInsight.controlFlow

import com.intellij.openapi.progress.ProgressManager
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import java.util.*
import kotlin.collections.ArrayDeque

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
        processPredecessors(instruction, processor, BitSet())
    }

    private fun processPredecessors(instruction: VlangInstruction, processor: VlangInstructionProcessor, visited: BitSet) {
        val pool = ArrayDeque<VlangInstruction>(50)
        pool.add(instruction)

        var counter = 0
        while (pool.isNotEmpty()) {
            val current = pool.first()

            if (!visited.get(current.id)) {
                visited.set(current.id)
                val result = current.process(processor)
                if (result) {
                    val predecessors = current.predecessors()
                    pool.addAll(predecessors)
                }
            }

            pool.removeFirst()

            if (counter % 100 == 0) {
                ProgressManager.checkCanceled()
            }

            counter++
        }
    }

    fun processSuccessors(instruction: VlangInstruction, processor: VlangInstructionProcessor) {
        return processSuccessors(instruction, processor, BitSet())
    }

    private fun processSuccessors(instruction: VlangInstruction, processor: VlangInstructionProcessor, visited: BitSet) {
        val pool = ArrayDeque<VlangInstruction>(50)
        pool.add(instruction)

        var counter = 0
        while (pool.isNotEmpty()) {
            val current = pool.first()

            if (!visited.get(current.id)) {
                val result = current.process(processor)
                if (result) {
                    current.appendSuccessors(pool)
                }
            }

            pool.removeFirst()

            if (counter % 100 == 0) {
                ProgressManager.checkCanceled()
            }

            counter++
        }
    }
}
