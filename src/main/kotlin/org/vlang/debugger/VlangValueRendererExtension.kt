package org.vlang.debugger

import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver.DebuggerLanguage
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.cidr.execution.debugger.evaluation.renderers.ValueRendererExtension

class VlangValueRendererExtension : ValueRendererExtension() {
    override fun handlesLanguage(debuggerLanguage: DebuggerLanguage): Boolean {
        return true
    }

    override fun getChildEvaluationExpression(
        cidrPhysicalValue: CidrPhysicalValue,
        s: String,
        cidrPhysicalValue1: CidrPhysicalValue
    ): String {
        return ""
    }
}