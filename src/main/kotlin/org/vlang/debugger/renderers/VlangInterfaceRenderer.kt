package org.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

object VlangInterfaceRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean {
        val type = value.type
        val fqn = VlangCTypeParser.convertCNameToVName(type)
        val klass = runReadAction {
            VlangClassLikeIndex.find(fqn, project, null, null).firstOrNull()
        }
        return klass is VlangInterfaceDeclaration
    }

    override fun getDisplayType(type: String): String = type

    override fun getData(value: VlangValue): LLValueData {
        val name = getInterfaceRealizationName(value) ?: return value.data
        val innerStruct = getInnerStructName(value) ?: return value.data.withDescription(name)

        val presentation = innerStruct.withContext(value.context).data.presentableValue
        if (presentation.startsWith("0x")) {
            return value.data.withDescription(
                buildString {
                    identifier(name)
                    append(" at ")
                    number(presentation)
                }
            )
        }

        return value.data.withDescription(name)
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val innerStruct = getInnerStructName(value) ?: return value.getVariableChildren(offset, size)
        return innerStruct.withContext(value.context).getVariableChildren(offset, size)
    }

    private fun getInnerStructName(value: VlangValue): LLValue? {
        val interfaceName = value.llValue.type
        val address = value.llValue.address?.toString(16) ?: return null

        val name = getInterfaceRealizationName(value) ?: return null
        val cName = getInterfaceCName(name)

        return value.context.evaluate("((($interfaceName*)(0x$address))->$cName)")
    }

    private fun getInterfaceCName(name: String): String = when (name) {
        "object"       -> "_object"
        "voidptr"      -> "_voidptr"
        else           -> if (name.contains(".")) {
            "_" + name.replace(".", "__")
        } else {
            "_main__$name"
        }
    }

    private fun getInterfaceRealizationName(value: VlangValue): String? {
        val interfaceName = value.llValue.type
        val address = value.llValue.address?.toString(16) ?: return null
        val typeIdx = value.context.evaluate("(($interfaceName*)(0x$address))->_typ").withContext(value.context).data.intValue()

        val getNameFunctionName = "v_typeof_interface_$interfaceName"
        val actualName = value.context.evaluate("$getNameFunctionName($typeIdx)").withContext(value.context).data.presentableValue
        return StringUtil.unquoteString(actualName)
    }
}
