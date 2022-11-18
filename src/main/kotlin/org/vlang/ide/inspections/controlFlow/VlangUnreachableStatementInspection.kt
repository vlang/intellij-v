package org.vlang.ide.inspections.controlFlow

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.codeInsight.controlFlow.VlangControlFlowUtil
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangEntryPointInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangStatementInstruction
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangScopeHolder
import org.vlang.lang.psi.VlangVisitor

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
