package org.vlang.debugger.renderers

import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext

class DebugValue(
    val context: VlangRendererEvaluationContext,
    var name: String,
    var type: String,
) {
    var value: String = ""

    val children: MutableList<DebugValue> = mutableListOf()

    fun toVL(): VlangValue = VlangFakeLLValue(name, type, type, null, null, "", data(),
        if (children.isEmpty()) null else children.map { it.toVL().llValue }).withContext(context)

    fun data(): LLValueData =
        LLValueData(value, null, false, children.isNotEmpty(), false)

    fun withChildren(vararg children: DebugValue): DebugValue {
        this.children.addAll(children)
        return this
    }

    fun withValue(value: String): DebugValue {
        this.value = value
        return this
    }

    companion object {
        fun from(value: VlangValue, withChildren: Boolean = true): DebugValue {
            return DebugValue(
                context = value.context,
                name = value.llValue.name,
                type = value.llValue.type,
            ).apply {
                if (withChildren) {
                    val children = value.context.getVariableChildren(value, 0, 100, true)
                    withChildren(*children.list.map { from(it) }.toTypedArray())
                }
            }
        }
    }
}
