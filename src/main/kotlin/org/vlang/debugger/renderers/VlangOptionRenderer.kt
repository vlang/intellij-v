package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription

object VlangOptionRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type.startsWith("_option_")

    override fun getDisplayType(type: String): String = "Option"

    override fun getData(value: VlangValue): LLValueData {
        val isSet = value["state"].data.presentableValue != "2 '\\x02'"
        if (!isSet) {
            return value.data.withDescription("None")
        }

        val data = getInnerData(value) ?: return value.data
        val presentable = data.data.presentableValue

        return value.data.withDescription(presentable)
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val isSet = value["state"].data.presentableValue == "0 '\\0'"
        if (!isSet) {
            return DebuggerDriver.ResultList.create(listOf(
                DebugValue(value.context, "value", "none")
                    .withValue("None").toVL()
            ), false)
        }

        val data = getInnerData(value) ?: return super.getVariableChildren(value, offset, size)
        return data.getVariableChildren(offset, size)
    }

    private fun getInnerData(value: VlangValue): VlangValue? {
        val innerType = VlangCTypeParser.parseOptionType(value.llValue.type)
        val cNameInnerType = VlangCTypeParser.toCName(innerType)
        val address = value.llValue.address ?: return null
        val dataAddress = address.toString(16)

        val data = value.evaluate("(*($cNameInnerType*)((_option_${cNameInnerType}*)0x$dataAddress)->data)")

        return data.withContext(value.context)
    }
}
