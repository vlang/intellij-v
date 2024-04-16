package io.vlang.ide.inspections.controlFlow

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.codeInsight.controlFlow.VlangControlFlowUtil
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import io.vlang.lang.codeInsight.controlFlow.instructions.VlangStatementInstruction
import io.vlang.lang.psi.VlangFunctionDeclaration
import io.vlang.lang.psi.VlangScopeHolder
import io.vlang.lang.psi.VlangVisitor

class VlangUnreachableStatementInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitFunctionDeclaration(o: VlangFunctionDeclaration) {
                analyzeScope(o, holder)
            }
        }
    }

    private fun analyzeScope(scopeHolder: VlangScopeHolder, holder: ProblemsHolder) {
        val controlFlow = scopeHolder.controlFlow()

        VlangControlFlowUtil.processFlow(controlFlow, object : VlangInstructionProcessor() {
            override fun processStatementInstruction(instruction: VlangStatementInstruction): Boolean {
                var reachable = false

                VlangControlFlowUtil.processPredecessors(instruction, object : VlangInstructionProcessor() {
                    override fun processEntryPointInstruction(instruction: VlangEntryPointInstruction): Boolean {
                        reachable = true
                        return false
                    }
                })

                if (!reachable) {
                    holder.registerProblem(instruction.statement, "Unreachable statement", ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                }

                return true
            }
        })
    }
}
