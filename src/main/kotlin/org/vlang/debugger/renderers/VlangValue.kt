package org.vlang.debugger.renderers

import com.jetbrains.cidr.execution.debugger.backend.DebuggerCommandException
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData

class VlangValue(val context: VlangRendererEvaluationContext, val llValue: LLValue) {
    val project = context.evaluationContext.project

    private val fields: Map<String, VlangValue> by lazy(LazyThreadSafetyMode.NONE) {
        getVariableChildren(0, 200, true).list.associateBy { it.name }
    }

    val name: String get() = llValue.name

    val isNullPointer: Boolean get() = data.isNullPointer

    val data: LLValueData get() = context.getData(llValue)

    fun getDataWithoutColors(): LLValueData =
        context.getData(llValue, false)

    fun evaluate(expression: String): LLValue =
        context.evaluate(expression)

    fun getVariableChildren(offset: Int, size: Int, raw: Boolean = false): DebuggerDriver.ResultList<VlangValue> =
        context.getVariableChildren(this, offset, size, raw)

    fun hasField(fieldName: String): Boolean =
        fields.containsKey(fieldName)

    operator fun get(fieldName: String): VlangValue =
        fields[fieldName]
            ?: throw DebuggerCommandException("Cannot find field with name $fieldName")

}