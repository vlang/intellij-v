package org.vlang.debugger.lang

import com.intellij.openapi.application.runReadAction
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.XSourcePosition
import com.jetbrains.cidr.execution.debugger.CidrEvaluator
import com.jetbrains.cidr.execution.debugger.CidrStackFrame
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.evaluation.CidrEvaluatedValue
import org.vlang.debugger.v2c.VlangExpressionTranspiler

class VlangEvaluator(frame: CidrStackFrame) : CidrEvaluator(frame) {
    override fun doEvaluate(driver: DebuggerDriver, position: XSourcePosition?, xexpr: XExpression): CidrEvaluatedValue {
        val expr = xexpr.expression
        if (position == null) {
            return evaluate(driver, null, expr)
        }

        val transpiler = VlangExpressionTranspiler()
        val result = runReadAction {
            transpiler.transpile(myFrame.process.project, position.file, position.offset, expr)
        }

        return evaluate(driver, position, result)
    }

    private fun evaluate(driver: DebuggerDriver, position: XSourcePosition?, result: String): CidrEvaluatedValue {
        val v = driver.evaluate(myFrame.thread, myFrame.frame, result)
        return CidrEvaluatedValue(v, myFrame.process, position, myFrame, result)
    }
}
