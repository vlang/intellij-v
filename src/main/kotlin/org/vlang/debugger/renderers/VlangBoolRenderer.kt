package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withDescription

object VlangBoolRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type == "bool" || value.type.startsWith("bool:")

    override fun getDisplayType(type: String): String = "bool"

    override fun getData(value: VlangValue): LLValueData {
        val isTrue = value.data.value.any { it == '1' }
        val text = if (isTrue) "true" else "false"
        return value.data.withDescription(
            buildString {
                keyword(text)
            }
        )
    }
}
