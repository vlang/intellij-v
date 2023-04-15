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

    fun getVariableChildrenWithType(elementTypeRaw: String, value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val len = value["len"].data.intValue()
        val data = value["data"]
        val dataAddress = data.llValue.address?.toString(16)

        var elementIsPointer = false
        var elementType = elementTypeRaw
        if (elementType.endsWith("_ptr")) {
            elementType = elementType.removeSuffix("_ptr")
            elementIsPointer = true
        }

        val pointer = if (elementIsPointer) "*" else ""

        if (dataAddress == null) {
            val elements = (0 until len)
                .asSequence()
                .mapIndexed { index, _ -> value.context.evaluate("(($elementType*$pointer)(${value.llValue.name}.data))[$index]") }
                .mapIndexed { index, it -> it.withName("$index") }
                .map { it.withContext(value.context) }
                .toList()

            return DebuggerDriver.ResultList.create(
                elements,
                false,
            )
        }

        // cast memory to fixed size array of elementType
        val array = value.evaluate("($pointer($elementType(*$pointer)[$len])(*(i64*)0x$dataAddress))")
        // and get children
        return array.withContext(value.context).getVariableChildren(offset, size, true)
    }
}
