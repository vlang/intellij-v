package org.vlang.ide.inspections.controlFlow

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.codeInsight.controlFlow.VlangControlFlowUtil
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangAccessVariableInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangVariableDeclarationInstruction
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangMutabilityOwner
import org.vlang.lang.psi.VlangScopeHolder
import org.vlang.lang.psi.VlangVisitor

class VlangVariableCanBeImmutableInspection : VlangBaseInspection() {
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
            override fun processVariableDeclarationInstruction(instruction: VlangVariableDeclarationInstruction): Boolean {
                val variable = instruction.definition
                if (!variable.isMutable()) {
                    return true
                }
                var written = false
                var hasAtLeastOneRead = false

                VlangControlFlowUtil.processSuccessors(instruction, object : VlangInstructionProcessor() {
                    override fun processAccessVariableInstruction(instruction: VlangAccessVariableInstruction): Boolean {
                        if (variable == instruction.definition) {
                            if (instruction.access == ReadWriteAccessDetector.Access.Write) {
                                written = true
                                return false
                            }

                            if (instruction.access == ReadWriteAccessDetector.Access.Read || instruction.access == ReadWriteAccessDetector.Access.ReadWrite) {
                                hasAtLeastOneRead = true
                            }
                        }
                        return true
                    }
                })

                if (!written && hasAtLeastOneRead) {
                    holder.registerProblem(
                        variable, "Variable can be immutable",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING, MAKE_IMMUTABLE_QUICK_FIX,
                    )
                }

                return true
            }
        })
    }

    companion object {
        val MAKE_IMMUTABLE_QUICK_FIX = object : LocalQuickFix {
            override fun getFamilyName() = "Make immutable"

            override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                val element = descriptor.psiElement as? VlangMutabilityOwner ?: return
                element.makeImmutable()
            }
        }
    }
}
