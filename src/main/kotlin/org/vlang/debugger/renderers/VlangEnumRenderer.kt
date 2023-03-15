package org.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.*
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

object VlangEnumRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean {
        val type = value.type.replace(alignRegex, "")
        val fqn = VlangCTypeParser.convertCNameToVName(type)
        val klass = runReadAction {
            VlangClassLikeIndex.find(fqn, project, null, null).firstOrNull()
        }
        return klass is VlangEnumDeclaration
    }

    override fun getDisplayType(type: String): String = "enum"

    override fun getData(value: VlangValue): LLValueData {
        if (value.hasField("key")) {
            return value.llValue.withContext(value.context).data
        }

        val data = value.context.getData(value.llValue)
        val name = data.value
        // main.Colors.red
        val enumFieldFqn= VlangCTypeParser.convertCNameToVName(name)
        // red
        val enumFieldName = enumFieldFqn.substringAfterLast(".")

        val intValue = getEnumIntValue(value)
        return value.data
            .withDescription(
                buildString {
                    identifier(enumFieldName)
                    append(" = ")
                    number(intValue.toString())
                }
            )
            .withChildren()
    }

    override fun getVariableChildren(
        value: VlangValue,
        offset: Int, size: Int,
    ): DebuggerDriver.ResultList<VlangValue> {
        if (value.hasField("key")) {
            return value.llValue.withContext(value.context).getVariableChildren(offset, size)
        }

        val intValue = getEnumIntValue(value)

        val ordinalValue = DebugValue(value.context, "ordinal", "int")
            .withValue(intValue.toString())

        return DebuggerDriver.ResultList.create(
            listOf(ordinalValue.toVL()),
            false,
        )
    }

    private fun getEnumIntValue(value: VlangValue): Long {
        val intValue = if (value.llValue.address != null) {
            val address = value.llValue.address!!.toString(16)
            val intValue = value.evaluate("((int*)(0x$address))[0]").withContext(value.context).data.intValue()
            intValue
        } else {
            0
        }
        return intValue
    }
}
