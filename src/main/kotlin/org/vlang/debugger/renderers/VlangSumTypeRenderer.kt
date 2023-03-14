package org.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.types.VlangPrimitiveTypes
import org.vlang.lang.stubs.index.VlangNamesIndex

object VlangSumTypeRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean {
        val type = value.type
        val fqn = VlangCTypeParser.convertCNameToVName(type)
        val sumType = runReadAction {
            VlangNamesIndex.find(fqn, project, null).firstOrNull()
        }
        if (sumType !is VlangTypeAliasDeclaration) return false
        val isAlias = runReadAction { sumType.aliasType?.isAlias ?: false }
        return !isAlias
    }

    override fun getDisplayType(type: String): String = type

    override fun getData(value: VlangValue): LLValueData {
        val name = getSumTypeConcreteName(value) ?: return value.data
        return value.data.withDescription(name)
    }

    override fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> {
        val sumtypeName = value.llValue.type
        val address = value.llValue.address?.toString(16) ?: return value.getVariableChildren(offset, size)

        val name = getSumTypeConcreteName(value) ?: return value.getVariableChildren(offset, size)
        val cName = getSumtypeCName(name)

        val concreteType = value.context.evaluate("((($sumtypeName*)(0x$address))->$cName)")
        return concreteType.withContext(value.context).getVariableChildren(offset, size)
    }

    private fun getSumtypeCName(name: String): String {
        val isPrimitive = VlangPrimitiveTypes.isPrimitiveType(name)
        if (isPrimitive) {
            return "_$name"
        }

        if (name.contains(".")) {
            return "_" + name.replace(".", "__")
        }

        return "_main__$name"
    }

    private fun getSumTypeConcreteName(value: VlangValue): String? {
        val sumtypeName = value.llValue.type
        val address = value.llValue.address?.toString(16) ?: return null
        val typeIdx = value.context.evaluate("(($sumtypeName*)(0x$address))->_typ").withContext(value.context).data.intValue()

        val getNameFunctionName = "v_typeof_sumtype_$sumtypeName"
        val actualName = value.context.evaluate("$getNameFunctionName($typeIdx)").withContext(value.context).data.presentableValue
        return StringUtil.unquoteString(actualName)
    }
}
