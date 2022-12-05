package org.vlang.debugger.runconfig

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.util.execution.ParametersListUtil
import org.vlang.ide.run.VlangProcessHandler
import org.vlang.ide.run.VlangRunConfiguration
import java.io.File

class VlangDebugConfigurationRunState(
    val env: ExecutionEnvironment,
    val conf: VlangRunConfiguration,
) : CommandLineState(env) {

    override fun startProcess(): ProcessHandler {
        val workingDir = conf.workingDir
        val outputDir = if (conf.outputDir.isEmpty()) File(conf.workingDir) else File(conf.outputDir)

        val exe = File(outputDir, "main")
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
