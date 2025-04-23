package io.vlang.ide.run

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.*
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
    val options: VlangRunConfigurationOptions.ExpandedOptions,
) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult? {

        if (!options.runAfterBuild) return null

        val state = object : CommandLineState(env) {
            override fun createConsole(executor: Executor): ConsoleView? {
                if (options.emulateTerminal) {
                    return TerminalExecutionConsole(env.project, null)
                }

                return super.createConsole(executor)
            }

            override fun startProcess(): ProcessHandler {
                val workingDir = options.workingDir
                val outputFileName = options.outputFileName

                val exe = if (!outputFileName.isEmpty()) {
                    val file = File(outputFileName)
                    if (file.isRooted) {
                        file
                    } else {
                        File(workingDir, outputFileName).normalize()
                    }
                } else {
                    val binName = VlangBuildTaskRunner.binaryName(options)
                    File(binName)
                }
                if (!exe.exists()) {
                    throw IllegalStateException("Can't run ${exe.absolutePath}, file not found")
                }

                val cmd = if (options.emulateTerminal) {
                    PtyCommandLine()
                        .withInitialColumns(PtyCommandLine.MAX_COLUMNS)
                        .withConsoleMode(true)
                } else {
                    GeneralCommandLine()
                }

                val commandLine = cmd
                    .withExePath(exe.absolutePath)
                    .withWorkDirectory(workingDir)
                    .withParentEnvironmentType(if (options.isPassParentEnvs) CONSOLE else NONE)
                    .withEnvironment(options.envsMap)
                    .withCharset(Charsets.UTF_8)
                    .withRedirectErrorStream(true)

                val additionalArguments = ParametersListUtil.parse(options.programArguments)
                commandLine.addParameters(additionalArguments)

                val handler = VlangProcessHandler(commandLine)

                // We need remove build artifacts after run if it's single file run conf.
                if (options.runKind == VlangRunConfigurationEditor.RunKind.File) {
                    handler.addProcessListener(object : ProcessListener {
                        override fun processTerminated(event: ProcessEvent) {
                            removeBuildArtifacts(exe)
                        }
                    })
                }

                return handler
            }

            private fun removeBuildArtifacts(exe: File) {
                exe.delete()
                val outputDir = exe.parentFile
                val debugSymbolDir = outputDir.resolve(VlangBuildTaskRunner.debugSymbolDir(options))
                if (debugSymbolDir.exists()) {
                    debugSymbolDir.deleteRecursively()
                }
            }
        }

        return state.execute(executor, runner)
    }
}
