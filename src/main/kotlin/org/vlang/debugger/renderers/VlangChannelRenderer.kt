package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withDescription

object VlangChannelRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type.startsWith("chan_")

    override fun getDisplayType(type: String): String = "chan"

    override fun getData(value: VlangValue): LLValueData {
        val closed = value["closed"].data.intValue() == 1L
        val cap = value["cap"].data.intValue()

        return value.data.withDescription("cap: $cap, closed: $closed")
    }
}
