package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription

object VlangIErrorRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type == "IError"

    override fun getDisplayType(type: String): String = "IError"

    override fun getData(value: VlangValue): LLValueData {
        val name = getInterfaceRealizationName(value) ?: return value.data
        return value.data.withDescription(name)
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val address = value.llValue.address?.toString(16) ?: return value.getVariableChildren(offset, size)

        val name = getInterfaceRealizationName(value) ?: return value.getVariableChildren(offset, size)
        val cName = getInterfaceCName(name)

        val innerStruct = value.context.evaluate("(((IError*)(0x$address))->$cName)")
        return innerStruct.withContext(value.context).getVariableChildren(offset, size)
    }

    private fun getInterfaceCName(name: String): String = when (name) {
        "object"       -> "_object"
        "None__"       -> "_None__"
        "voidptr"      -> "_voidptr"
        "Error"        -> "_Error"
        "MessageError" -> "_MessageError"
        else           -> if (name.contains(".")) {
            "_" + name.replace(".", "__")
        } else {
            "_main__$name"
        }
    }

    private fun getInterfaceRealizationName(value: VlangValue): String? {
        val address = value.llValue.address?.toString(16) ?: return null
        val nameValue = value.context.evaluate("v_typeof_interface_IError(((IError*)(0x$address))->_typ)")
        return StringUtil.unquoteString(nameValue.withContext(value.context).data.presentableValue)
    }
}
