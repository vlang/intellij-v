package org.vlang.lang.codeInsight.controlFlow

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.impl.*
import org.vlang.lang.psi.*

open class VlangControlFlowBuilder(private val scopeHolder: PsiElement) {
    private val instructions = mutableListOf<VlangInstruction>()
    private val markerInstructions = mutableListOf<VlangMarkerInstruction>()
    private val deferStatements = mutableListOf<VlangDeferStatement>()
    private val entryPointInstruction = VlangEntryPointInstructionImpl(scopeHolder)
    private val exitPointInstruction = VlangExitPointInstructionImpl()
    private val deferMarkerInstruction = VlangDeferMarkerInstruction()

    private var lastInstruction: VlangInstructionImpl? = null
    private val visitor = VlangControlFlowBuilderVisitor()

    fun build(): VlangControlFlow {
        addInstruction(entryPointInstruction)

        scopeHolder.acceptChildren(visitor)

        processDefers()
        addInstruction(exitPointInstruction)

        for (markerInstruction in markerInstructions) {
            val predecessors = markerInstruction.predecessors()
            predecessors.remove(markerInstruction)
            val successor = markerInstruction.successor()

            if (successor != null) {
                for (predecessor in predecessors) {
                    predecessor as VlangInstructionImpl
                    predecessor.replaceSuccessor(markerInstruction, successor)
                }

                successor.predecessors().remove(markerInstruction)
                successor.predecessors().addAll(predecessors)
            }

            markerInstruction.dispose()
        }

        instructions.forEachIndexed { index, instruction ->
            instruction.id = index
        }

        return VlangControlFlowImpl(instructions)
    }

    private fun processDefers() {
        addInstruction(deferMarkerInstruction)
        for (deferStatement in deferStatements) {
            addStatementInstruction(deferStatement)
            val deferInstruction = VlangDeferInstructionImpl(deferStatement)
            val block = deferStatement.block
            block.accept(visitor)
            addInstruction(deferInstruction)
        }
    }

    inner class VlangControlFlowBuilderVisitor : VlangRecursiveElementVisitor() {
        override fun visitFunctionDeclaration(o: VlangFunctionDeclaration) {
            addInstruction(VlangFunctionDeclarationInstructionImpl(o))
            o.acceptChildren(this)
        }

        override fun visitVarDeclaration(decl: VlangVarDeclaration) {
            addStatementInstruction(decl)
            val vars = decl.varDefinitionList

            for (variable in vars) {
                addInstruction(VlangVariableDeclarationInstructionImpl(variable))
            }
        }

        override fun visitAssignmentStatement(assign: VlangAssignmentStatement) {
            addStatementInstruction(assign)
            val exprs = assign.leftHandExprList.expressionList

            for (expr in exprs) {
                val refExpr = expr as? VlangReferenceExpression
                val resolved = refExpr?.resolve() ?: continue

                if (resolved is VlangVarDefinition) {
                    addInstruction(
                        VlangAccessVariableInstructionImpl(
                            expr,
                            resolved,
                            ReadWriteAccessDetector.Access.Write
                        )
                    )
                }

                if (resolved is VlangFieldDefinition) {
                    addInstruction(
                        VlangAccessFieldInstructionImpl(
                            expr,
                            resolved,
                            ReadWriteAccessDetector.Access.Write
                        )
                    )
                }

                val qualifier = refExpr.getQualifier()
                visitQualifier(qualifier) { next ->
                    if (next is VlangVarDefinition) {
                        addInstruction(
                            VlangAccessVariableInstructionImpl(
                                expr,
                                next,
                                ReadWriteAccessDetector.Access.Write
                            )
                        )
                    }

                    if (next is VlangFieldDefinition) {
                        addInstruction(
                            VlangAccessFieldInstructionImpl(
                                expr,
                                next,
                                ReadWriteAccessDetector.Access.Write
                            )
                        )
                    }
                }
            }
        }

        private fun visitQualifier(qualifier: VlangCompositeElement?, onResolved: (PsiElement) -> Unit) {
            if (qualifier == null) return
            val refExpr = qualifier as? VlangReferenceExpression
            val resolved = refExpr?.resolve()
            if (resolved != null) {
                onResolved(resolved)
            }

            val next = refExpr?.getQualifier() ?: return
            visitQualifier(next, onResolved)
        }

        override fun visitReferenceExpression(expr: VlangReferenceExpression) {
            val resolved = expr.resolve()

            if (resolved is VlangVarDefinition) {
                addInstruction(VlangAccessVariableInstructionImpl(expr, resolved, expr.readWriteAccess))
            }

            if (resolved is VlangParamDefinition) {
                addInstruction(VlangAccessParameterInstructionImpl(expr, resolved, expr.readWriteAccess))
            }

            if (resolved is VlangFieldDefinition) {
                addInstruction(VlangAccessFieldInstructionImpl(expr, resolved, expr.readWriteAccess))
            }

            val qualifier = expr.getQualifier()
            visitQualifier(qualifier) { next ->
                if (next is VlangVarDefinition) {
                    addInstruction(VlangAccessVariableInstructionImpl(qualifier!!, next, expr.readWriteAccess))
                }

                if (next is VlangParamDefinition) {
                    addInstruction(VlangAccessParameterInstructionImpl(qualifier!!, next, expr.readWriteAccess))
                }

                if (next is VlangFieldDefinition) {
                    addInstruction(VlangAccessFieldInstructionImpl(qualifier!!, next, expr.readWriteAccess))
                }
            }
        }

        override fun visitIfExpression(o: VlangIfExpression) {
            addStatementInstruction(o)

            val condition = o.expression ?: return // TODO
            val ifOutInstruction = VlangIfOutMarkerInstruction()
            val ifConditionHost = VlangHostInstructionImpl(condition)
            val trueConditionInstruction = VlangConditionInstructionImpl(condition, true)
            val falseConditionInstruction = VlangConditionInstructionImpl(condition, false)

            condition.accept(this)
            addInstruction(ifConditionHost)
            addInstruction(trueConditionInstruction)
            val thenBlock = o.block
            thenBlock?.accept(this)
            jump(ifOutInstruction)
            lastInstruction = ifConditionHost
            addInstruction(falseConditionInstruction)

            val elseBranch = o.elseBranch
            val elseBlock = elseBranch?.block
            elseBlock?.accept(this)

            addInstruction(ifOutInstruction)
        }

        override fun visitOrBlockExpr(o: VlangOrBlockExpr) {
            addStatementInstruction(o)

            val condition = o.expression ?: return
            val outInstruction = VlangIfOutMarkerInstruction()
            val conditionHost = VlangHostInstructionImpl(condition)
            val trueConditionInstruction = VlangConditionInstructionImpl(condition, true)
            val falseConditionInstruction = VlangConditionInstructionImpl(condition, false)

            condition.accept(this)
            addInstruction(conditionHost)
            addInstruction(trueConditionInstruction)
            o.block.accept(this)
            jump(outInstruction)
            lastInstruction = conditionHost
            addInstruction(falseConditionInstruction)

            addInstruction(outInstruction)
        }

        override fun visitReturnStatement(ret: VlangReturnStatement) {
            addStatementInstruction(ret)
            val results = ret.expressionList
            for (result in results) {
                result.accept(this)
            }

            addInstruction(VlangExitMarkerInstruction())
            addInstruction(VlangReturnInstructionImpl(ret))
            jump(deferMarkerInstruction)
        }

        override fun visitDeferStatement(o: VlangDeferStatement) {
            deferStatements.add(o)
        }

        override fun visitCallExpr(o: VlangCallExpr) {
            addStatementInstruction(o)
            val resolved = (o.expression as? VlangReferenceExpression)?.resolve()

            o.argumentList.accept(this)

            if (resolved is VlangFunctionDeclaration) {
                addInstruction(VlangFunctionCallInstructionImpl(o, resolved))

                if (resolved.isNoReturn) {
                    addInstruction(VlangExitMarkerInstruction())
                    jump(deferMarkerInstruction)
                }
            }
        }
    }

    private fun addStatementInstruction(element: PsiElement) {
        val statement = element.parentOfType<VlangStatement>(true)
        if (statement != null) {
            addInstruction(VlangStatementInstructionImpl(statement))
        }
    }

    private fun addInstruction(instruction: VlangInstructionImpl) {
        lastInstruction?.join(instruction)

        lastInstruction = instruction

        if (instruction is VlangMarkerInstruction) {
            markerInstructions.add(instruction)
        } else {
            instructions.add(instruction)
        }
    }

    private fun jump(endInstruction: VlangInstructionImpl) {
        if (lastInstruction != null) {
            lastInstruction?.join(endInstruction)
            interruptFlow()
        }
    }

    private fun interruptFlow() {
        lastInstruction = null
    }

    private open class VlangMarkerInstruction : VlangLinearInstructionImpl(null) {
        fun successor() = successor

        fun dispose() {
            successor = null
            predecessors().clear()
        }
    }

    private class VlangIfOutMarkerInstruction : VlangMarkerInstruction()
    private class VlangDeferMarkerInstruction : VlangMarkerInstruction()
    private class VlangExitMarkerInstruction : VlangMarkerInstruction()
}
