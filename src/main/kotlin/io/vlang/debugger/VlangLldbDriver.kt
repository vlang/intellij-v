package io.vlang.debugger

import com.intellij.openapi.application.runReadAction
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
     * If transpilation fails, returns the original expression to allow simple C-compatible conditions to work.
     */
    private fun transpileCondition(condition: String, filePath: String, line: Int): String {
        val proj = project ?: return condition
        val virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath) ?: return condition

        return try {
            val transpiler = VlangExpressionTranspiler()
            runReadAction {
                // Use line number to estimate offset (approximate)
                transpiler.transpile(proj, virtualFile, null, condition)
            }
        } catch (_: Exception) {
            // If transpilation fails, return original condition
            // This allows simple C-compatible conditions like "i == 5" to work
            condition
        }
    }
}
