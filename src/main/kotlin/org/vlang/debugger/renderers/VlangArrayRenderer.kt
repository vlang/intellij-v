package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription
import org.vlang.debugger.withName

object VlangArrayRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean {
        return value.type.startsWith("Array_")
    }

    override fun getDisplayType(type: String): String = "array"

    override fun getData(value: VlangValue): LLValueData {
        if (value.hasField("key")) {
            return value.llValue.withContext(value.context).data
        }

        val len = value["len"].data.intValue()
        val cap = value["cap"].data.intValue()

        return value.data.withDescription("len: $len, cap: $cap")
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        if (value.hasField("key")) {
            return value.llValue.withContext(value.context).getVariableChildren(offset, size)
        }
        val elementType = VlangCTypeParser.parseArrayType(value.llValue.type)
        return getVariableChildrenWithType(elementType, value, offset, size)
    }

    fun getVariableChildrenWithType(elementType: String, value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val len = value["len"].data.intValue()
        val data = value["data"]
        val dataAddress = data.llValue.address?.toString(16)

        if (dataAddress == null) {
            val elements = (0 until len)
                .asSequence()
                .mapIndexed { index, it -> value.context.evaluate("(($elementType*)(${value.llValue.name}.data))[$index]") }
                .mapIndexed { index, it -> it.withName("$index") }
                .map { it.withContext(value.context) }
                .toList()

            return DebuggerDriver.ResultList.create(
                elements,
                false,
            )
        }

        // cast memory to fixed size array of elementType
        val array = value.evaluate("(($elementType(*)[$len])(*(i64*)0x$dataAddress))")
        // and get children
        return array.withContext(value.context).getVariableChildren(offset, size, true)
    }
}
