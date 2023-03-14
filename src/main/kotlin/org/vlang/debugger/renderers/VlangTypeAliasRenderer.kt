package org.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangStringTypeEx
import org.vlang.lang.stubs.index.VlangNamesIndex

object VlangTypeAliasRenderer : VlangValueRenderer() {
    private var alias: VlangTypeAliasDeclaration? = null

    override fun isApplicable(project: Project, value: LLValue): Boolean {
        val type = value.type
        val fqn = VlangCTypeParser.convertCNameToVName(type)
        val alias = runReadAction {
            VlangNamesIndex.find(fqn, project, null).firstOrNull()
        }
        if (alias !is VlangTypeAliasDeclaration) return false
        val isAlias = runReadAction { alias.aliasType?.isAlias ?: false }
        if (!isAlias) return false

        this.alias = alias
        return true
    }

    override fun getDisplayType(type: String): String = type

    override fun getData(value: VlangValue): LLValueData {
        val alias = this.alias ?: return value.data
        val aliasedType = runReadAction { alias.aliasType?.typeUnionList?.typeList?.firstOrNull()?.toEx() } ?: return value.data
        if (aliasedType is VlangStringTypeEx) {
            return VlangStringRenderer.getData(value)
        }

        val qualifiedName = runReadAction { alias.getQualifiedName() }
        if (qualifiedName == "strings.Builder") {
            val address = value.llValue.address ?: return value.data
            val dataAddress = address.toString(16)
            val strValue = value.context.evaluate("strings__Builder_after((strings__Builder*)(0x$dataAddress), 0)")
            return strValue.withContext(value.context).data
        }

        return value.data
    }
}
