package io.vlang.debugger

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.jetbrains.cidr.ArchitectureType
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriverConfiguration
import io.vlang.debugger.runconfig.VlangLLDBDriverConfiguration
import io.vlang.debugger.v2c.VlangExpressionTranspiler

class VlangLldbDriver(
    handler: Handler,
    private val vlangStarter: LLDBDriverConfiguration,
    architectureType: ArchitectureType,
) : LLDBDriver(handler, vlangStarter, architectureType) {

    private val project: Project?
        get() = (vlangStarter as? VlangLLDBDriverConfiguration)?.project

    override fun addBreakpoint(
        file: String,
        line: Int,
        condition: String?,
        temporary: Boolean
    ): DebuggerDriver.AddBreakpointResult {
        val transpiledCondition = condition?.let { transpileCondition(it, file, line) }
        return super.addBreakpoint(file, line, transpiledCondition, temporary)
    }

    /**
     * Transpiles a V breakpoint condition expression to C syntax for LLDB.
     * Tries regex-based pattern matching first for known patterns (string
     * comparisons), then falls back to PSI-based transpilation.
     * Regex-first avoids PSI resolution failures for local variables that
     * can't be resolved in the file-level PSI context.
     */
    private fun transpileCondition(condition: String, filePath: String, line: Int): String {
        // Fast path: handle known patterns directly without PSI resolution
        val fast = fallbackTranspile(condition)
        if (fast != condition) return fast

        // Slow path: full PSI-based transpilation for complex expressions
        val proj = project ?: return condition
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath)
            ?: return condition

        return try {
            val transpiler = VlangExpressionTranspiler()
            runReadAction {
                val document = FileDocumentManager.getInstance().getDocument(virtualFile)
                val offset = document?.let {
                    val zeroBasedLine = (line - 1).coerceIn(0, it.lineCount - 1)
                    it.getLineStartOffset(zeroBasedLine)
                }
                transpiler.transpile(proj, virtualFile, offset, condition)
            }
        } catch (_: Exception) {
            condition
        }
    }

    /**
     * Regex-based fallback for when PSI transpilation fails (e.g. variable
     * can't be resolved in the file's PSI context).  Handles the most common
     * breakpoint condition pattern: string field compared to a literal.
     *
     * `job.part_num == '2601-3255'`  →  `strcmp((char*)(job.part_num).str, "2601-3255") == 0`
     */
    private fun fallbackTranspile(condition: String): String {
        STRING_COMPARE_PATTERN.matchEntire(condition.trim())?.let { m ->
            val lhs = m.groupValues[1].trim()
            val op = m.groupValues[2]
            val strValue = m.groupValues[3]
            return """strcmp((char*)($lhs).str, "$strValue") $op 0"""
        }
        return condition
    }

    companion object {
        // Matches:  expr == 'str'  |  expr != "str"
        private val STRING_COMPARE_PATTERN =
            """(.+?)\s*(==|!=)\s*['"](.+?)['"]""".toRegex()
    }
}
