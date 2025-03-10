package io.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import io.vlang.debugger.withContext
import io.vlang.debugger.withDescription
import io.vlang.debugger.withHasLongerDescription

class VlangStringRenderer : VlangValueRenderer() {
    override fun isApplicable(project: Project, value: LLValue): Boolean = value.type == "string" || value.type == "string *"

    override fun getDisplayType(type: String): String = "string"

    override fun getData(value: VlangValue): LLValueData {
        if (value.hasField("key")) {
            return value.llValue.withContext(value.context).data
        }

        val needHighlight = value.context.withColors

        val strField = value["str"].llValue
        val data = value.context.getData(strField)
        val text = data.description ?: return value.data
        return value.data
            .withDescription(if (needHighlight) highlightString(text) else text)
            .withHasLongerDescription(needLongerDescription(text))
    }
}
