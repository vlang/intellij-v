package org.vlang.debugger.renderers

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.backend.LLValueData
import com.jetbrains.cidr.execution.debugger.evaluation.renderers.ValueRendererUtils
import org.vlang.debugger.withDescription
import org.vlang.debugger.withMayHaveChildren
import org.vlang.debugger.withName

abstract class VlangValueRenderer {
    abstract fun isApplicable(project: Project, value: LLValue): Boolean

    protected open fun getDisplayType(type: String): String =
        type

    open fun getData(value: VlangValue): LLValueData =
        value.data

    protected open fun getChildrenCount(value: VlangValue): Int? =
        null

    open fun getVariableChildren(value: VlangValue, offset: Int, size: Int): DebuggerDriver.ResultList<VlangValue> =
        value.getVariableChildren(offset, size)

    companion object {
        val EP_NAME: ExtensionPointName<VlangValueRenderer> =
            ExtensionPointName.create("org.vlang.debuggerValueRenderer")

        /**
         * [alignRegex] is used to remove alignment from the description of the value.
         * For example, the description of the value `int` is `int:4` and we need to
         * remove the `:4` part.
         */
        val alignRegex = ":\\d+$".toRegex()

        /**
         * [processPointer] renders the value of the pointer in the debugger.
         * This function is needed to handle pointers uniformly in all renderers.
         */
        fun processPointer(value: VlangValue): LLValueData? {
            val description = value.data.description ?: value.data.value
            if (value.data.isNullPointer) {
                return value.data.withName(
                    buildString {
                        keyword("nil")
                        number(" (0x0)")
                    }
                ).withMayHaveChildren(false)
            }
            if (description.startsWith("0x")) {
                return value.data.withDescription(
                    buildString {
                        number(description)
                    }
                )
            }
            return null
        }

        /**
         * [needLongerDescription] checks if the description of the value need button
         * to show the full description in the debugger.
         * We also show the button if the description contains newlines or tabs to make
         * it easier to read in popup window.
         */
        fun needLongerDescription(text: String): Boolean = text.length > 100 || text.contains("\\n") || text.contains("\\t")

        /**
         * [highlightString] highlights the string in the debugger.
         */
        fun highlightString(str: String): String = str
            .replace("\\n", "\\n".keyword())
            .replace("\\t", "\\t".keyword())
            .string()

        private fun String.string(): String = "${MARK_CHAR}S$this${MARK_CHAR}E"
        private fun String.keyword(): String = "${MARK_CHAR}K$this${MARK_CHAR}E"
        private fun String.number(): String = "${MARK_CHAR}N$this${MARK_CHAR}E"
        private fun String.comment(): String = "${MARK_CHAR}C$this${MARK_CHAR}E"
        private fun String.identifier(): String = "${MARK_CHAR}V$this${MARK_CHAR}E"

        fun StringBuilder.string(str: String): StringBuilder = append(str.string())
        fun StringBuilder.keyword(str: String): StringBuilder = append(str.keyword())
        fun StringBuilder.number(str: String): StringBuilder = append(str.number())
        fun StringBuilder.comment(str: String): StringBuilder = append(str.comment())
        fun StringBuilder.identifier(str: String): StringBuilder = append(str.identifier())

        fun String.removeRendererMarks(): String =
            ValueRendererUtils.extractString(this)
                .replace("${MARK_CHAR}S", "")
                .replace("${MARK_CHAR}K", "")

        private const val MARK_CHAR = 0xFE.toChar()
    }
}
