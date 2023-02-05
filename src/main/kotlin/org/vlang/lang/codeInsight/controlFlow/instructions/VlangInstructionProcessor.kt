package org.vlang.lang.codeInsight.controlFlow.instructions

abstract class VlangInstructionProcessor {
    open fun processInstruction(instruction: VlangInstruction): Boolean {
        return true
    }

    open fun processEntryPointInstruction(instruction: VlangEntryPointInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processExitPointInstruction(instruction: VlangExitPointInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processStatementInstruction(instruction: VlangStatementInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processFunctionCallInstruction(instruction: VlangFunctionCallInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processHostInstruction(instruction: VlangHostInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processConditionInstruction(instruction: VlangConditionInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processFunctionDeclarationInstruction(instruction: VlangFunctionDeclarationInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processReturnInstruction(instruction: VlangReturnInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processDeferInstruction(instruction: VlangDeferInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processVariableDeclarationInstruction(instruction: VlangVariableDeclarationInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processAccessInstruction(instruction: VlangAccessInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processAccessVariableInstruction(instruction: VlangAccessVariableInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processAccessParameterInstruction(instruction: VlangAccessParameterInstruction): Boolean {
        return processInstruction(instruction)
    }

    open fun processAccessFieldInstruction(instruction: VlangAccessFieldInstruction): Boolean {
        return processInstruction(instruction)
    }
}
