package io.vlang.ide.run

import com.intellij.build.BuildViewManager
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.*
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.task.*
import com.intellij.util.execution.ParametersListUtil
import io.vlang.configurations.VlangConfigurationUtil
import io.vlang.configurations.VlangProjectSettingsConfigurable
import io.vlang.debugger.runconfig.VlangDebugRunner
import io.vlang.notifications.VlangErrorNotification
import io.vlang.notifications.VlangNotification
import io.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.rejectedPromise
import org.jetbrains.concurrency.resolvedPromise
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
        val options = conf.expandedOptions()

        val workingDir = File(options.workingDir)
        val outputFileName = options.outputFileName
        val outputDir = File(outputFileName).parentFile

        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs()
        }

        val pathToBuild = if (options.runKind == VlangRunConfigurationEditor.RunKind.Directory) {
            options.directory
        } else {
            options.fileName
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
            .withParentEnvironmentType(if (options.isPassParentEnvs) CONSOLE else NONE)
            .withEnvironment(options.envsMap)
            .withParameters(pathToBuild)
            .withParameters("-color")
            .withWorkDirectory(workingDir)

        if (options.production) {
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

        if (!outputFileName.isEmpty()) {
            commandLine.withParameters("-o", File(outputFileName).absolutePath)
        }

        val additionalArguments = ParametersListUtil.parse(options.buildArguments)
        commandLine.addParameters(additionalArguments)

        val handler = VlangProcessHandler(commandLine)
        ctx.processHandler = handler
        ctx.workingDirectory = workingDir.toPath()
        handler.addProcessListener(VlangBuildAdapter(ctx, buildProgressListener))
        handler.addProcessListener(object : ProcessListener {
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
        fun binaryName(options: VlangRunConfigurationOptions.ExpandedOptions): String {
            val name = if (options.runKind == VlangRunConfigurationEditor.RunKind.File) {
                val file = File(options.fileName)
                val execFile = File(file.parentFile, file.nameWithoutExtension).normalize().toString()
                if (file.isRooted) {
                    execFile
                } else {
                    File(options.workingDir, execFile).normalize().toString()
                }
            } else {
                File(options.workingDir).resolve(File(options.directory)).normalize().name
            }
            if (SystemInfo.isWindows) {
                return "$name.exe"
            }
            return name
        }

        fun debugSymbolDir(options: VlangRunConfigurationOptions.ExpandedOptions): String {
            val name = File(options.fileName).nameWithoutExtension
            return "$name.dSYM"
        }
    }
}
