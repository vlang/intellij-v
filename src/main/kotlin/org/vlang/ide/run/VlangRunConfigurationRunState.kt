package org.vlang.ide.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import org.vlang.configurations.VlangProjectSettingsState
import java.io.File

class VlangRunConfigurationRunState(
    env: ExecutionEnvironment,
    private val conf: VlangRunConfiguration,
) : CommandLineState(env) {

    override fun startProcess(): ProcessHandler {
        val file = File(conf.scriptName)
        val workingDir = file.parentFile

        // TODO: show notification with link to setup toolchain
        val exe = VlangProjectSettingsState.getInstance(conf.project).compilerLocation
            ?: throw RuntimeException("V executable not found, toolchain not setup correctly?")

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
