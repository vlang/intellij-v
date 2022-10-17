package org.vlang.ide.run

import com.intellij.build.BuildContentDescriptor
import com.intellij.build.BuildContentManager
import com.intellij.build.BuildViewManager
import com.intellij.build.DefaultBuildDescriptor
import com.intellij.build.events.impl.*
import com.intellij.build.output.BuildOutputInstantReaderImpl
import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.ExecutorRegistry
import com.intellij.execution.actions.StopProcessAction
import com.intellij.execution.impl.ExecutionManagerImpl
import com.intellij.execution.process.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.util.ThreeState
import javax.swing.JComponent

@Suppress("UnstableApiUsage")
class VlangBuildAdapter(
    private val ctx: VlangBuildContext,
    private val buildProgressListener: BuildViewManager,
) : ProcessAdapter() {

    private val instantReader = BuildOutputInstantReaderImpl(
        ctx.buildId,
        ctx.parentId,
        buildProgressListener,
        listOf(VlangBuildEventsConverter(ctx))
    )

    init {
        val buildToolWindow = BuildContentManager.getInstance(ctx.project).orCreateToolWindow
        buildToolWindow.setAvailable(true, null)
        buildToolWindow.activate(null)

        val buildContentDescriptor = BuildContentDescriptor(null, null, object : JComponent() {}, "Build")
        val activateToolWindow = true
        buildContentDescriptor.isActivateToolWindowWhenAdded = activateToolWindow
        buildContentDescriptor.isActivateToolWindowWhenFailed = activateToolWindow
        buildContentDescriptor.isNavigateToError = ThreeState.YES

        val processHandler = ctx.processHandler
        val descriptor = DefaultBuildDescriptor(ctx.buildId, "Run V Build", ctx.workingDirectory.toString(), ctx.started)
            .withContentDescriptor { buildContentDescriptor }
            .withRestartAction(createRerunAction(processHandler, ctx.environment))
            .withRestartAction(createStopAction(processHandler))

        val buildStarted = StartBuildEventImpl(descriptor, "${ctx.taskName} running...")
        buildProgressListener.onEvent(ctx.buildId, buildStarted)
    }

    override fun processTerminated(event: ProcessEvent) {
        instantReader.closeAndGetFuture().whenComplete { _, error ->
            val isSuccess = event.exitCode == 0 && ctx.errors.get() == 0
            val isCanceled = /*ctx.indicator?.isCanceled ?:*/ false
            onBuildOutputReaderFinish(event, isSuccess = isSuccess, isCanceled = isCanceled, error)
        }
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        // Progress messages end with '\r' instead of '\n'. We want to replace '\r' with '\n'
        // so that `instantReader` sends progress messages to parsers separately from other messages.
        val text = StringUtil.convertLineSeparators(event.text)
        instantReader.append(text)
    }

    private fun onBuildOutputReaderFinish(
        event: ProcessEvent,
        isSuccess: Boolean,
        isCanceled: Boolean,
        error: Throwable?,
    ) {
        val (status, result) = when {
            isCanceled -> "canceled" to SkippedResultImpl()
            isSuccess  -> "successful" to SuccessResultImpl()
            else       -> "failed" to FailureResultImpl(error)
        }

        val buildFinished = FinishBuildEventImpl(
            ctx.buildId,
            null,
            System.currentTimeMillis(),
            "${ctx.taskName} $status",
            result
        )
        buildProgressListener.onEvent(ctx.buildId, buildFinished)

        ctx.environment.notifyProcessTerminated(event.processHandler, event.exitCode)

        val targetDir = VfsUtil.findFile(ctx.workingDirectory, true) ?: return
        VfsUtil.markDirtyAndRefresh(true, true, true, targetDir)
    }

    override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {
        ctx.environment.notifyProcessTerminating(event.processHandler)
    }

    companion object {
        private val ExecutionEnvironment.executionListener: ExecutionListener
            get() = project.messageBus.syncPublisher(ExecutionManager.EXECUTION_TOPIC)

        fun ExecutionEnvironment.notifyProcessTerminated(handler: ProcessHandler, exitCode: Int) =
            executionListener.processTerminated(executor.id, this, handler, exitCode)

        fun ExecutionEnvironment.notifyProcessTerminating(handler: ProcessHandler) =
            executionListener.processTerminating(executor.id, this, handler)

        private fun createStopAction(processHandler: ProcessHandler): StopProcessAction =
            StopProcessAction("Stop", "Stop", processHandler)

        private fun createRerunAction(processHandler: ProcessHandler, environment: ExecutionEnvironment): RestartProcessAction =
            RestartProcessAction(processHandler, environment)

        private class RestartProcessAction(
            private val processHandler: ProcessHandler,
            private val environment: ExecutionEnvironment,
        ) : DumbAwareAction() {
            private val isEnabled: Boolean
                get() {
                    val project = environment.project
                    val settings = environment.runnerAndConfigurationSettings
                    return (!DumbService.isDumb(project) || settings == null || settings.type.isDumbAware) &&
                            !ExecutorRegistry.getInstance().isStarting(environment) &&
                            !processHandler.isProcessTerminating
                }

            override fun update(event: AnActionEvent) {
                val presentation = event.presentation
                presentation.text = "Rerun '${StringUtil.escapeMnemonics(environment.runProfile.name)}'"
                presentation.icon = if (processHandler.isProcessTerminated) AllIcons.Actions.Compile else AllIcons.Actions.Restart
                presentation.isEnabled = isEnabled
            }

            override fun getActionUpdateThread(): ActionUpdateThread {
                return ActionUpdateThread.BGT
            }

            override fun actionPerformed(event: AnActionEvent) {
                ExecutionManagerImpl.stopProcess(processHandler)
                ExecutionUtil.restart(environment)
            }
        }
    }
}
