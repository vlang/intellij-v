package org.vlang.lang.codeInsight.controlFlow

import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import org.vlang.lang.VlangTypes
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
        override fun visitElement(o: VlangElement) {
            o.acceptChildren(this)
        }

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

            super.visitElement(decl)
        }

        override fun visitAssignmentStatement(assign: VlangAssignmentStatement) {
            addStatementInstruction(assign)
            val exprs = assign.leftHandExprList.expressionList

            for (expr in exprs) {
                expr.accept(this)
            }

            val rightExprs = assign.expressionList
            for (expr in rightExprs) {
                expr.accept(this)
            }
        }

        override fun visitReferenceExpression(expr: VlangReferenceExpression) {
            addInstruction(VlangAccessInstructionImpl(expr, expr.readWriteAccess))

            val qualifier = expr.getQualifier()
            qualifier?.accept(this)
        }

        override fun visitMatchExpression(o: VlangMatchExpression) {
            addStatementInstruction(o)

            val expression = o.expression ?: return
            val matchConditionHost = VlangHostInstructionImpl(expression)
            val matchOutInstruction = VlangIfOutMarkerInstruction()

            expression.accept(this)
            addInstruction(matchConditionHost)

            for (arm in o.arms) {
                for (param in arm.parameterList) {
                    addInstruction(VlangConditionInstructionImpl(param, true))
                }
                arm.block.accept(this)

                jump(matchOutInstruction)
                lastInstruction = matchConditionHost
            }

            o.elseArm?.let { arm ->
                addInstruction(VlangConditionInstructionImpl(null, false))
                arm.block?.accept(this)
            }

            addInstruction(matchOutInstruction)
        }

        override fun visitMatchElseArmClause(o: VlangMatchElseArmClause) {
            o.block?.accept(this)
        }

        override fun visitIfExpression(o: VlangIfExpression) {
            addStatementInstruction(o)

            val condition = o.expression ?: o.guardVarDeclaration ?: return // TODO
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
            elseBranch?.block?.accept(this)
            elseBranch?.ifExpression?.accept(this)

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

            o.expression?.accept(this)
            o.argumentList.accept(this)

            addInstruction(VlangFunctionCallInstructionImpl(o))

            if (o.identifier != null && o.identifier!!.textMatches("panic")) {
                addInstruction(VlangExitMarkerInstruction())
                jump(deferMarkerInstruction)
            }
        }

        override fun visitIsExpression(o: VlangIsExpression) {
            o.expression.accept(this)
            o.type?.accept(this)
        }

        override fun visitBinaryExpr(o: VlangBinaryExpr) {
            val op = o.operator
            if (op.elementType == VlangTypes.COND_AND) {
                processShortCircuitExpression(o, trueBranchFirst = false)
                return
            }

            if (op.elementType == VlangTypes.COND_OR) {
                processShortCircuitExpression(o, trueBranchFirst = true)
                return
            }

            super.visitBinaryExpr(o)
        }

        private fun processShortCircuitExpression(o: VlangBinaryExpr, trueBranchFirst: Boolean) {
            val outInstruction = VlangMarkerInstruction()
            val leftOperand = o.left
            val conditionHost = VlangHostInstructionImpl(o)
            val trueConditionInstruction = VlangConditionInstructionImpl(leftOperand, true)
            val falseConditionInstruction = VlangConditionInstructionImpl(leftOperand, false)

            leftOperand.accept(this)

            addInstruction(conditionHost)
            addInstruction(if (trueBranchFirst) trueConditionInstruction else falseConditionInstruction)

            val shortCircuitInstruction = if (trueBranchFirst) falseConditionInstruction else trueConditionInstruction
            addInstruction(shortCircuitInstruction)

            o.right?.accept(this)

            addInstruction(outInstruction)
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
