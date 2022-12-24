package org.vlang.ide.test.configuration

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView
import com.intellij.openapi.project.guessProjectDir
import com.intellij.util.execution.ParametersListUtil
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import java.io.File

class VlangTestConfigurationRunState(
    env: ExecutionEnvironment,
    private val conf: VlangTestConfiguration,
) : RunProfileState {

    override fun execute(exec: Executor?, runner: ProgramRunner<*>): ExecutionResult? {
        return startProcess(exec)
    }

    private fun startProcess(exec: Executor?): ExecutionResult? {
        if (exec == null) {
            return null
        }

        val file = File(conf.filename)

        val exe = conf.project.toolchainSettings.toolchain().compiler()
            ?: throw RuntimeException(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)
        val workingDir = conf.project.guessProjectDir()?.toNioPath()?.toFile() ?: file.parentFile.parentFile

        val commandLine = GeneralCommandLine()
            .withExePath(exe.path)
            .withWorkDirectory(workingDir)
            .withEnvironment(mapOf("VJOBS" to "1"))
            .withParameters("-test-runner")
            .withParameters("teamcity")
            .withParameters("-stats")
            .withParameters("test")

        if (conf.scope == VlangTestScope.Directory) {
            commandLine.withParameters(conf.directory)
        }

        if (conf.scope == VlangTestScope.File) {
            commandLine.withParameters(conf.filename)
        }

        if (conf.scope == VlangTestScope.Function) {
            if (conf.pattern.isNotEmpty()) {
                commandLine.withParameters("-run-only")
                commandLine.withParameters(conf.pattern)
            }
            commandLine.withParameters(conf.filename)
        }

        val additionalArguments = ParametersListUtil.parse(conf.additionalParameters)
        commandLine.addParameters(additionalArguments)

        val consoleProperties = VlangTestConsoleProperties(conf, exec)
        val console = SMTestRunnerConnectionUtil
            .createConsole(
                consoleProperties.testFrameworkName,
                consoleProperties
            ) as SMTRunnerConsoleView

        val handler = KillableColoredProcessHandler(commandLine)
        console.attachToProcess(handler)

        val connection = conf.project.messageBus.connect()
        connection.subscribe(SMTRunnerEventsListener.TEST_STATUS, object : SMTRunnerEventsAdapter() {
            override fun onBeforeTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
                testsRoot.setFinished()
            }
        })

        val smTestProxy = console.resultsViewer.root as SMTestProxy.SMRootTestProxy
        smTestProxy.setTestsReporterAttached()

        return DefaultExecutionResult(console, handler)
    }
}
