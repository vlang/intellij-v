package org.vlang.ide.run

import com.intellij.build.BuildViewManager
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.task.*
import com.intellij.util.execution.ParametersListUtil
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.resolvedPromise
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import java.io.File

@Suppress("UnstableApiUsage")
class VlangBuildTaskRunner : ProjectTaskRunner() {
    override fun run(project: Project, context: ProjectTaskContext, vararg tasks: ProjectTask): Promise<Result> {
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

        val exe = project.projectSettings.compilerLocation
            ?: throw RuntimeException(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)

        val commandLine = GeneralCommandLine()
            .withExePath(exe)
            .withEnvironment(conf.envs)
            .withParameters(pathToBuild)
            .withParameters("-color")
            .withWorkDirectory(workingDir)

        if (outputDir != null) {
            commandLine.withParameters("-o", File(outputDir, "main").absolutePath)
        } else {
            commandLine.withParameters("-o", File(workingDir, "main").absolutePath)
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
}
