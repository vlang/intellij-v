package io.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import io.vlang.debugger.withDescription
import io.vlang.lang.psi.types.VlangPrimitiveTypes.Companion.isNumeric

class VlangPrimitiveTypesRenderer : VlangValueRenderer() {

    override fun isApplicable(project: Project, value: LLValue): Boolean = isNumeric(value.type) || value.type == "rune"

    override fun getDisplayType(type: String): String = type

    override fun getData(value: VlangValue): LLValueData {
        return value.data.withDescription(
            buildString {
                if (value.llValue.type == "u8") {
                    string(value.data.value)
                } else {
                    number(value.data.value)
                }
            }
        )
    }
}
