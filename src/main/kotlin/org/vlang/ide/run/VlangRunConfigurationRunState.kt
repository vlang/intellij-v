package org.vlang.ide.run

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.util.execution.ParametersListUtil
import java.io.File

class VlangRunConfigurationRunState(
    val env: ExecutionEnvironment,
    val conf: VlangRunConfiguration,
) : RunProfileState {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult? {
        if (!conf.runAfterBuild) return null

        val state = object : CommandLineState(env) {
            override fun startProcess(): ProcessHandler {
                val workingDir = conf.workingDir
                val outputDir = if (conf.outputDir.isEmpty()) File(conf.workingDir) else File(conf.outputDir)

                val binName = File(conf.fileName).nameWithoutExtension
                val exe = File(outputDir, binName)
                if (!exe.exists()) {
                    throw IllegalStateException("Can't run ${exe.absolutePath}, file not found")
                }

                val commandLine = GeneralCommandLine()
                    .withExePath(exe.absolutePath)
                    .withWorkDirectory(workingDir)
                    .withCharset(Charsets.UTF_8)
                    .withRedirectErrorStream(true)

                val additionalArguments = ParametersListUtil.parse(conf.programArguments)
                commandLine.addParameters(additionalArguments)

                return VlangProcessHandler(commandLine)
            }
        }

        return state.execute(executor, runner)
    }
}
