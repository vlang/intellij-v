package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withDescription
import org.vlang.debugger.withName

object VlangClosureContextRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type.endsWith("_Ctx *")

    override fun getDisplayType(type: String): String = "Closure Context"

    override fun getData(value: VlangValue): LLValueData {
        return value.data.withName("captured variables")
    }
}
