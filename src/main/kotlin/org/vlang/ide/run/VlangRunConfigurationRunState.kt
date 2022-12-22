package org.vlang.ide.run

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ConsoleView
import com.intellij.terminal.TerminalExecutionConsole
import com.intellij.util.execution.ParametersListUtil
import java.io.File

class VlangRunConfigurationRunState(
    val env: ExecutionEnvironment,
    val conf: VlangRunConfiguration,
) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult? {
        if (!conf.runAfterBuild) return null

        val state = object : CommandLineState(env) {
            override fun createConsole(executor: Executor): ConsoleView? {
                if (conf.emulateTerminal) {
                    return TerminalExecutionConsole(conf.project, null)
                }

                return super.createConsole(executor)
            }

            override fun startProcess(): ProcessHandler {
                val workingDir = conf.workingDir
                val outputDir = if (conf.outputDir.isEmpty()) File(conf.workingDir) else File(conf.outputDir)

                val binName = VlangBuildTaskRunner.binaryName(conf)
                val exe = File(outputDir, binName)
                if (!exe.exists()) {
                    throw IllegalStateException("Can't run ${exe.absolutePath}, file not found")
                }

                val cmd = if (conf.emulateTerminal) {
                    PtyCommandLine()
                        .withInitialColumns(PtyCommandLine.MAX_COLUMNS)
                        .withConsoleMode(true)
                } else {
                    GeneralCommandLine()
                }

                val commandLine = cmd
                    .withExePath(exe.absolutePath)
                    .withWorkDirectory(workingDir)
                    .withCharset(Charsets.UTF_8)
                    .withRedirectErrorStream(true)

                val additionalArguments = ParametersListUtil.parse(conf.programArguments)
                commandLine.addParameters(additionalArguments)

                val handler = VlangProcessHandler(commandLine)

                // We need remove build artifacts after run if it's single file run conf.
                if (conf.runKind == VlangRunConfigurationEditor.RunKind.File) {
                    handler.addProcessListener(object : ProcessListener {
                        override fun processTerminated(event: ProcessEvent) {
                            removeBuildArtifacts(exe, outputDir)
                        }
                    })
                }

                return handler
            }

            private fun removeBuildArtifacts(exe: File, outputDir: File) {
                exe.delete()
                val debugSymbolDir = outputDir.resolve(VlangBuildTaskRunner.debugSymbolDir(conf))
                if (debugSymbolDir.exists()) {
                    debugSymbolDir.deleteRecursively()
                }
            }
        }

        return state.execute(executor, runner)
    }
}
