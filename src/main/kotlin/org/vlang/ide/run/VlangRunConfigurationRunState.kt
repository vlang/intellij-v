package org.vlang.ide.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import java.io.File

class VlangRunConfigurationRunState(
    env: ExecutionEnvironment,
    private val conf: VlangRunConfiguration,
) : CommandLineState(env) {

    override fun startProcess(): ProcessHandler {
        val file = File(conf.scriptName)
        val workingDir = file.parentFile

        val exe = conf.project.projectSettings.compilerLocation
            ?: throw RuntimeException(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)

        val commandLine = GeneralCommandLine()
            .withExePath(exe)
            .withParameters("run", conf.scriptName)
            .withWorkDirectory(workingDir)

        if (conf.additionalParameters.isNotEmpty()) {
            commandLine.withParameters(conf.additionalParameters.split(" "))
        }

        return KillableColoredProcessHandler(commandLine)
    }
}
