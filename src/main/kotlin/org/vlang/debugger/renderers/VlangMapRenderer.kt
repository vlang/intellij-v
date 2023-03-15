package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.*

object VlangMapRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type.startsWith("Map_")

    override fun getDisplayType(type: String): String = "map"

    override fun getData(value: VlangValue): LLValueData {
        val len = value["len"].data.intValue()
        return value.data.withDescription("len: $len")
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val type = value.llValue.type
        val (keyType, rawValueType) = VlangCTypeParser.parseMapType(type)
        val valueType = VlangCTypeParser.convertPointerTypeToC(rawValueType)

        val varRef = if (value.llValue.address != null) {
            "(struct map*)0x" + value.llValue.address?.toString(16)
        } else {
            "&" + value.llValue.name
        }

        val keys = value.context.evaluate("map_keys($varRef)")
        val values = value.context.evaluate("map_values($varRef)")

        val keyData = VlangArrayRenderer.getVariableChildrenWithType(keyType, keys.withContext(value.context), offset, size).list
        val valueData = VlangArrayRenderer.getVariableChildrenWithType(valueType, values.withContext(value.context), offset, size).list

        return DebuggerDriver.ResultList.create(
            keyData.zip(valueData) { key, value ->
                val keyValue = key.llValue
                    .withData(
                        name = key.getDataWithoutColors().presentableValue,
                        data = LLValueData(key.data.value, value.data.presentableValue, false, true, false),
                        children = listOf(
                            key.llValue.withName("key"),
                            value.llValue.withName("value"),
                        )
                    )
                    .withContext(value.context)

                keyValue
            },
            false
        )
    }
}
