package org.vlang.debugger.renderers

import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.lang.VlangLldbEvaluationContext
import org.vlang.debugger.mapItems
import org.vlang.debugger.withContext

class VlangRendererEvaluationContext(
    val evaluationContext: VlangLldbEvaluationContext,
    root: LLValue,
    var withColors: Boolean = true,
) {
    private val dataStack = mutableSetOf(root)
    private val childStack = mutableSetOf(root)

    fun getData(value: LLValue, withColors: Boolean = true): LLValueData {
        if (dataStack.add(value)) {
            val withColorsInitial = evaluationContext.withColors
            evaluationContext.withColors = withColors

            val data = evaluationContext.getData(value)

            evaluationContext.withColors = withColorsInitial
            dataStack.remove(value)
            return data
        }

        return evaluationContext.getRawData(value)
    }

    fun evaluate(expression: String): LLValue =
        evaluationContext.evaluate(expression)

    fun getVariableChildren(
        value: VlangValue,
        offset: Int,
        size: Int,
        raw: Boolean,
    ): DebuggerDriver.ResultList<VlangValue> {
        if (!raw && childStack.add(value.llValue))
            try {
                val renderer = evaluationContext.findRenderer(value.llValue)
                if (renderer != null) {
                    return renderer.getVariableChildren(value, offset, size)
                }
            } finally {
                childStack.remove(value.llValue)
            }
        return evaluationContext.getRawVariableChildren(value.llValue, offset, size)
            .mapItems { it.withContext(this) }
    }
}