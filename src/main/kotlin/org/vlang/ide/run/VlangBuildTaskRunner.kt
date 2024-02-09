package org.vlang.ide.run

import com.intellij.build.BuildViewManager
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.task.*
import com.intellij.util.execution.ParametersListUtil
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.rejectedPromise
import org.jetbrains.concurrency.resolvedPromise
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.configurations.VlangProjectSettingsConfigurable
import org.vlang.debugger.runconfig.VlangDebugRunner
import org.vlang.notifications.VlangErrorNotification
import org.vlang.notifications.VlangNotification
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import java.io.File

@Suppress("UnstableApiUsage")
class VlangBuildTaskRunner : ProjectTaskRunner() {
    override fun run(project: Project, context: ProjectTaskContext, vararg tasks: ProjectTask): Promise<Result> {
        if (project.isDisposed) {
            return rejectedPromise("Project is already disposed")
        }

        val resultPromise = AsyncPromise<Result>()

        tasks.forEach { task ->
            if (task !is ProjectModelBuildTask<*>) {
                return resolvedPromise(TaskRunnerResults.ABORTED)
            }

            val conf = task.buildableElement as? VlangBuildConfiguration ?: return@forEach
            val env = conf.environment
            val configuration = conf.configuration

            invokeLater {
                val buildId = Any()
                startProcess(
                    VlangBuildContext(
                        project, env, "Build",
                        buildId, buildId,
                    ), configuration, resultPromise
                )
            }
        }

        return resultPromise
    }

    private fun startProcess(
        ctx: VlangBuildContext,
        conf: VlangRunConfiguration,
        resultPromise: AsyncPromise<Result>,
    ) {
        val workingDir = File(conf.workingDir)
        val outputDir = if (conf.outputDir.isEmpty()) null else File(conf.outputDir)

        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs()
        }

        val pathToBuild = if (conf.runKind == VlangRunConfigurationEditor.RunKind.Directory) {
            conf.directory
        } else {
            conf.fileName
        }

        val project = conf.project
        val buildProgressListener = project.service<BuildViewManager>()

        val exe = project.toolchainSettings.toolchain().compiler()
        if (exe == null) {
            VlangErrorNotification(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)
                .withTitle("Can't build V project")
                .withActions(VlangNotification.Action("Setup V toolchain") { _, notification ->
                    VlangProjectSettingsConfigurable.show(project)
                    notification.expire()
                })
                .show()
            resultPromise.setResult(TaskRunnerResults.FAILURE)
            return
        }

        val commandLine = GeneralCommandLine()
            .withExePath(exe.path)
            .withEnvironment(conf.envs)
            .withParameters(pathToBuild)
            .withParameters("-color")
            .withWorkDirectory(workingDir)

        if (conf.production) {
            commandLine.withParameters("-prod")
        }

        val isDebug = ctx.environment.runner is VlangDebugRunner
        if (isDebug) {
            commandLine.withParameters("-g")
            commandLine.addParameters("-no-parallel", "-no-retry-compilation", /*"-skip-unused"*/)
            commandLine.addParameters("-keepc")

            // Debugging with TCC not working on Windows and Linux for some reasons
            if (SystemInfo.isLinux) {
                commandLine.withParameters("-cc", "gcc")
            } else if (SystemInfo.isWindows) {
                commandLine.withParameters("-cc", "msvc")
            }
        }

        val binName = binaryName(conf)
        if (outputDir != null) {
            commandLine.withParameters("-o", File(outputDir, binName).absolutePath)
        } else {
            commandLine.withParameters("-o", File(workingDir, binName).absolutePath)
        }

        val additionalArguments = ParametersListUtil.parse(conf.buildArguments)
        commandLine.addParameters(additionalArguments)

        val handler = VlangProcessHandler(commandLine)
        ctx.processHandler = handler
        ctx.workingDirectory = workingDir.toPath()
        handler.addProcessListener(VlangBuildAdapter(ctx, buildProgressListener))
        handler.addProcessListener(object : ProcessAdapter() {
            override fun processTerminated(event: ProcessEvent) {
                if (event.exitCode == 0) {
                    resultPromise.setResult(TaskRunnerResults.SUCCESS)
                } else {
                    resultPromise.setResult(TaskRunnerResults.FAILURE)
                }
            }
        })
        handler.startNotify()
    }

    override fun canRun(projectTask: ProjectTask): Boolean {
        return when (projectTask) {
            is ModuleBuildTask          -> true
            is ProjectModelBuildTask<*> -> true
            else                        -> false
        }
    }

    companion object {
        fun binaryName(conf: VlangRunConfiguration): String {
            val name = File(conf.fileName).nameWithoutExtension
            if (SystemInfo.isWindows) {
                return "$name.exe"
            }
            return name
        }

        fun debugSymbolDir(conf: VlangRunConfiguration): String {
            val name = File(conf.fileName).nameWithoutExtension
            return "$name.dSYM"
        }
    }
}
