package org.vlang.debugger.runconfig

import com.intellij.execution.ExecutionManager
import com.intellij.execution.configurations.*
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.util.PlatformUtils
import com.intellij.util.execution.ParametersListUtil
import org.jetbrains.concurrency.resolvedPromise
import org.vlang.ide.run.VlangBuildTaskRunner
import org.vlang.ide.run.VlangRunConfigurationRunState
import org.vlang.ide.test.configuration.VlangTestConfiguration
import java.io.File
import java.util.concurrent.ExecutionException

@Suppress("UnstableApiUsage", "DEPRECATION")
open class VlangDebugRunner : ProgramRunner<RunnerSettings> {
    override fun getRunnerId() = RUNNER_ID

    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        if (profile is VlangTestConfiguration) {
            return false
        }

        return executorId == DefaultDebugExecutor.EXECUTOR_ID
    }

    override fun execute(environment: ExecutionEnvironment) {
        val state = environment.state ?: return
        @Suppress("UnstableApiUsage")
        ExecutionManager.getInstance(environment.project).startRunProfile(environment) {
            resolvedPromise(doExecute(state, environment))
        }
    }

    private fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        if (state !is VlangRunConfigurationRunState) return null
        assertAvailability()

        val conf = state.conf
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

        return showRunContent(environment, commandLine)
    }

    private fun showRunContent(
        environment: ExecutionEnvironment,
        runExecutable: GeneralCommandLine,
    ): RunContentDescriptor = VlangDebugRunnerUtils.showRunContent(environment, runExecutable)

    private fun assertAvailability() {
        if (!PlatformUtils.isIdeaUltimate() &&
            !PlatformUtils.isCidr() &&
            !PlatformUtils.isPyCharmPro() &&
            !PlatformUtils.isGoIde() &&
            !PlatformUtils.isRubyMine() &&
            !PlatformUtils.isRider()
        ) {
            throw ExecutionException(
                "Debugging is currently supported in IntelliJ IDEA Ultimate, PyCharm Professional, CLion, GoLand, RubyMine and Rider.", null
            )
        }
    }

    companion object {
        const val RUNNER_ID: String = "VlangDebugRunner"
    }
}
