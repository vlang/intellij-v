package org.vlang.debugger.renderers

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData

abstract class VlangValueRenderer {
    abstract fun isApplicable(project: Project, value: LLValue): Boolean

    protected open fun getDisplayType(type: String): String =
        type

    open fun getData(value: VlangValue): LLValueData =
        value.data

    protected open fun getChildrenCount(value: VlangValue): Int? =
        null

    open fun getVariableChildren(
        value: VlangValue, offset: Int, size: Int
    ): DebuggerDriver.ResultList<VlangValue> =
        value.getVariableChildren(offset, size)

    companion object {
        val EP_NAME: ExtensionPointName<VlangValueRenderer> =
            ExtensionPointName.create("org.vlang.debuggerValueRenderer")
    }
}
