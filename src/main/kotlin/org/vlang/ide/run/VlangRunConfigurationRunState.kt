package org.vlang.ide.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import org.vlang.sdk.VlangSdkService
import java.io.File

class VlangRunConfigurationRunState(
    env: ExecutionEnvironment,
    private val conf: VlangRunConfiguration,
) : CommandLineState(env) {

    override fun startProcess(): ProcessHandler {
        val file = File(conf.scriptName)
        val workingDir = file.parentFile

        val exe = VlangSdkService.getInstance(conf.project).getExecutable()
            ?: throw RuntimeException("V executable not found, SDK not setup correctly?")

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
