package io.vlang.debugger.runconfig

import com.intellij.execution.configurations.*
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.AsyncProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.util.EnvironmentUtil
import com.intellij.util.execution.ParametersListUtil
import io.vlang.ide.run.VlangBuildTaskRunner
import io.vlang.ide.run.VlangRunConfigurationRunState
import io.vlang.ide.test.configuration.VlangTestConfiguration
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.resolvedPromise
import java.io.File


open class VlangDebugRunner : AsyncProgramRunner<RunnerSettings>() {
    override fun getRunnerId() = RUNNER_ID

    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        if (profile is VlangTestConfiguration) {
            return false
        }

        return executorId == DefaultDebugExecutor.EXECUTOR_ID
    }

    override fun execute(environment: ExecutionEnvironment, state: RunProfileState): Promise<RunContentDescriptor?> {
        return resolvedPromise(doExecute(state, environment))
    }

    private fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        if (state !is VlangRunConfigurationRunState) return null

        val options = state.options
        val workingDir = options.workingDir
        val outputFileName = options.outputFileName

        val env = EnvironmentUtil.getEnvironmentMap() + EnvironmentUtil.parseEnv(options.envs.split("\n", ",").toTypedArray()).apply {
            EnvironmentUtil.inlineParentOccurrences(this)
        }

        val binName = if (!outputFileName.isEmpty()) {
            outputFileName
        } else {
            VlangBuildTaskRunner.binaryName(options)
        }

        val exe = if (File(binName).isAbsolute) {
            File(binName)
        } else {
            File(workingDir, binName)
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
            .withCharset(Charsets.UTF_8)
            .withRedirectErrorStream(true)
            .withEnvironment(env)

        val additionalArguments = ParametersListUtil.parse(options.programArguments)
        commandLine.addParameters(additionalArguments)

        return showRunContent(environment, commandLine)
    }

    private fun showRunContent(
        environment: ExecutionEnvironment,
        runExecutable: GeneralCommandLine,
    ): RunContentDescriptor = VlangDebugRunnerUtils.showRunContent(environment, runExecutable)

    companion object {
        const val RUNNER_ID: String = "VlangDebugRunner"
    }
}
