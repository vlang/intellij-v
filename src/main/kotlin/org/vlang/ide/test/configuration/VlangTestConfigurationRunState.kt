package org.vlang.ide.test.configuration

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import java.io.File

class VlangTestConfigurationRunState(
    env: ExecutionEnvironment,
    private val conf: VlangTestConfiguration,
) : CommandLineState(env) {

    override fun startProcess(): ProcessHandler {
        val file = File(conf.testFile)

        val exe = conf.project.projectSettings.compilerLocation
            ?: throw RuntimeException(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)

        val commandLine = GeneralCommandLine()
            .withExePath(exe)

        if (conf.testModule.isNotEmpty()) {
            val workingDir = file.parentFile.parentFile
            commandLine
                .withParameters("test", conf.testModule)
                .withWorkDirectory(workingDir)
        } else {
            val workingDir = file.parentFile
            commandLine
                .withParameters(conf.testFile)
                .withWorkDirectory(workingDir)
        }

        if (conf.additionalParameters.isNotEmpty()) {
            commandLine.withParameters(conf.additionalParameters.split(" "))
        }

        return KillableColoredProcessHandler(commandLine)
    }
}
