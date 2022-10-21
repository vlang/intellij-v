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
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView
import com.intellij.util.execution.ParametersListUtil
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
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

        val file = File(conf.testFile)

        val exe = conf.project.projectSettings.compilerLocation
            ?: throw RuntimeException(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)
        val workingDir = file.parentFile.parentFile

        val commandLine = GeneralCommandLine()
            .withExePath(exe)
            .withWorkDirectory(workingDir)
            .withParameters("test")

        val testUnit = if (conf.testModule.isEmpty() || conf.testModule == "main") conf.testFile else conf.testModule

        commandLine.withParameters(testUnit)

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

        val smTestProxy = console.resultsViewer.root as SMTestProxy.SMRootTestProxy
        smTestProxy.setTestsReporterAttached()
        smTestProxy.setSuiteStarted()
        smTestProxy.setStarted()

        return DefaultExecutionResult(console, handler)
    }
}
