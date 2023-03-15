package org.vlang.debugger.renderers

import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import org.vlang.debugger.withContext
import org.vlang.debugger.withDescription
import org.vlang.debugger.withHasLongerDescription

object VlangStringRenderer : VlangValueRenderer() {
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
