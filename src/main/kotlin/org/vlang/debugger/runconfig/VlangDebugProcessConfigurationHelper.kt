package org.vlang.debugger.runconfig

import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.DebuggerCommandException
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.backend.gdb.GDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import org.vlang.debugger.lang.VlangDebuggerState
import java.nio.file.InvalidPathException

class VlangDebugProcessConfigurationHelper(private val process: CidrDebugProcess) {
    private val threadId = process.currentThreadId
    private val frameIndex = process.currentFrameIndex

    fun configure() {
        process.postCommand { driver ->
            try {
                val state = VlangDebuggerState.getInstance()
                if (state.stopsAtPanics) {
                    driver.setBreakOnPanic()
                }

                if (state.dontStepIntoGeneratedFunctions) {
                    driver.setSteppingFilters()
                }

                driver.setBreakForGC()
            } catch (e: DebuggerCommandException) {
                process.printlnToConsole(e.message)
            } catch (_: InvalidPathException) {
            }
        }
    }

    private fun DebuggerDriver.setBreakForGC() {
        val commands = when (this) {
            is LLDBDriver -> listOf(
                "breakpoint set -n main -G true -o true -C 'process handle -s false -n false SIGSEGV SIGBUS'",
                "breakpoint set -n main__main -G true -o true -C 'process handle -s true -n true SIGSEGV SIGBUS'"
            )

            else          -> return
        }
        for (command in commands) {
            executeInterpreterCommand(threadId, frameIndex, command)
        }
    }

    private fun DebuggerDriver.setBreakOnPanic() {
        val commands = when (this) {
            is LLDBDriver -> listOf("breakpoint set -n panic_debug")
            is GDBDriver  -> listOf("set breakpoint pending on", "break panic_debug")
            else          -> return
        }
        for (command in commands) {
            executeInterpreterCommand(threadId, frameIndex, command)
        }
    }

    private fun DebuggerDriver.setSteppingFilters() {
        val regexes = mutableListOf<String>()
        regexes.add("^v_typeof_.*")
        regexes.add("^I_.*to_Interface.*")
        regexes.add("^memdup$")
        regexes.add("^indent_.*")
        regexes.add("^str_intp.*")

        val command = when (this) {
            is LLDBDriver -> "settings set target.process.thread.step-avoid-regexp"
            is GDBDriver  -> "skip -rfu"
            else          -> return
        }

        val regex = regexes.joinToString("|")
        executeInterpreterCommand(threadId, frameIndex, "$command $regex")
    }
}
