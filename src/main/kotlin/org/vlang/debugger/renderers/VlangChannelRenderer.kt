package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription

object VlangChannelRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type.startsWith("chan_")

    override fun getDisplayType(type: String): String = "chan"

    override fun getData(value: VlangValue): LLValueData {
        val closed = value["closed"].data.intValue() != 0L
        val cap = value["cap"].data.intValue()
        val available = value["read_avail"].data.intValue()

        return value.data.withDescription("cap: $cap, available for reading: $available, closed: $closed")
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val elementType = VlangCTypeParser.parseChannelType(value.llValue.type)
        val buffer = value["ringbuf"]
        val cap = value["cap"].data.intValue()
        val available = value["read_avail"].data.intValue()
        val address = buffer.llValue.address ?: return super.getVariableChildren(value, offset, size)

        val readIndex = value["buf_elem_read_idx"].data.intValue()
        val dataAddress = address.toString(16)

        // cast memory to fixed size array of elementType
        val array = value.evaluate("(($elementType(*)[$cap])(*(i64*)0x$dataAddress))")

        val arrayChildren = array.withContext(value.context).getVariableChildren(0, cap.toInt(), true)
        val bufferChildren = mutableListOf<VlangValue>()

        for (i in 0 until available) {
            val index = (readIndex + i) % cap
            val child = arrayChildren.list[index.toInt()]
            bufferChildren.add(child)
        }

        return DebuggerDriver.ResultList.create(bufferChildren, false)
    }
}
