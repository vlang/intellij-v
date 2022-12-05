package org.vlang.debugger.lang

import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.XSourcePosition
import com.jetbrains.cidr.execution.debugger.CidrEvaluator
import com.jetbrains.cidr.execution.debugger.CidrStackFrame
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.evaluation.CidrEvaluatedValue

class VlangEvaluator(frame: CidrStackFrame) : CidrEvaluator(frame) {
    override fun doEvaluate(driver: DebuggerDriver, position: XSourcePosition?, expr: XExpression): CidrEvaluatedValue {
        val v = driver.evaluate(myFrame.thread, myFrame.frame, expr.expression)
        return CidrEvaluatedValue(v, myFrame.process, position, myFrame, expr.expression)
    }
}
