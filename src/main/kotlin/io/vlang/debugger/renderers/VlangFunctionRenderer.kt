package io.vlang.debugger.renderers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import io.vlang.debugger.withDescription
import io.vlang.lang.psi.VlangFunctionType
import io.vlang.lang.psi.VlangTypeAliasDeclaration
import io.vlang.lang.stubs.index.VlangNamesIndex

class VlangFunctionRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean {
        val type = value.type
        if (type.contains("(*)")) {
            return true
        }

        val fqn = VlangCTypeParser.convertCNameToVName(type)
        val alias = runReadAction {
            VlangNamesIndex.find(fqn, project, null).firstOrNull()
        }
        if (alias !is VlangTypeAliasDeclaration) {
            return false
        }
        val aliasType = runReadAction { alias.aliasType } ?: return false
        val isAlias = runReadAction { aliasType.isAlias }
        if (!isAlias) {
            return false
        }

        val inner = runReadAction { aliasType.typeUnionList?.typeList?.firstOrNull() } ?: return false
        return inner is VlangFunctionType
    }

    override fun getDisplayType(type: String): String = type

    override fun getData(value: VlangValue): LLValueData {
        val data = value.data
        val description = data.description ?: return data

        val content = description.removeSurrounding("(", ")").substringAfter("`")

        val rawFunctionName = content.substringBeforeLast(" at ")
        val functionName = VlangCTypeParser.convertCNameToVName(rawFunctionName)
        val defPos = content.substringAfterLast(" at ")

        if (functionName.startsWith("anon_fn_")) {
            return data.withDescription(
                buildString {
                    identifier("anonymous function")
                    comment(" defined at ")
                    identifier(defPos)
                }
            )
        }

        return data.withDescription(
            buildString {
                identifier(functionName)
                comment(" defined at ")
                identifier(defPos)
            }
        )
    }
}
