package org.vlang.debugger.runconfig

import com.intellij.execution.ExecutionManager
import com.intellij.execution.configurations.*
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.task.*
import com.intellij.util.execution.ParametersListUtil
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBBinUrlProvider
import org.jetbrains.concurrency.resolvedPromise
import org.vlang.ide.run.VlangBuildTaskRunner
import java.io.File

class VlangDebugRunner : ProgramRunner<RunnerSettings> {
    override fun getRunnerId() = RUNNER_ID

    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        return executorId == DefaultDebugExecutor.EXECUTOR_ID
    }

    override fun execute(environment: ExecutionEnvironment) {
        val state = environment.state ?: return
        @Suppress("UnstableApiUsage")
        ExecutionManager.getInstance(environment.project).startRunProfile(environment) {
            resolvedPromise(doExecute(state, environment))
        }
    }

    protected fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        if (state !is VlangDebugConfigurationRunState) return null

        LLDBBinUrlProvider.lldb.macX64
        LLDBBinUrlProvider.lldbFrontend.macX64

        val workingDir = state.conf.workingDir
        val outputDir = if (state.conf.outputDir.isEmpty()) File(state.conf.workingDir) else File(state.conf.outputDir)

        val binName = VlangBuildTaskRunner.binaryName(state.conf)
        val exe = File(outputDir, binName)
        if (!exe.exists()) {
            throw IllegalStateException("Can't run ${exe.absolutePath}, file not found")
        }

        val commandLine = GeneralCommandLine()
            .withExePath(exe.absolutePath)
            .withWorkDirectory(workingDir)
            .withCharset(Charsets.UTF_8)
            .withRedirectErrorStream(true)

        val additionalArguments = ParametersListUtil.parse(state.conf.programArguments)
        commandLine.addParameters(additionalArguments)

        return showRunContent(state, environment, commandLine)
    }

    fun showRunContent(
        state: CommandLineState,
        environment: ExecutionEnvironment,
        runExecutable: GeneralCommandLine
    ): RunContentDescriptor = VlangDebugRunnerUtils.showRunContent(state, environment, runExecutable)

    companion object {
        const val RUNNER_ID: String = "VlangDebugRunner"
    }
}
