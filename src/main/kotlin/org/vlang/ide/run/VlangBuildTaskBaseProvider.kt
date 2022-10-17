package org.vlang.ide.run

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.Key
import com.intellij.task.ProjectTaskManager
import java.util.concurrent.CompletableFuture

abstract class VlangBuildTaskBaseProvider<T : VlangBuildTaskBaseProvider.BuildTask<T>> : BeforeRunTaskProvider<T>() {
    override fun getName() = "Build V"
    override fun getIcon() = AllIcons.Actions.Compile
    override fun isSingleton() = true

    protected fun doExecuteTask(buildConfiguration: VlangRunConfiguration, environment: ExecutionEnvironment): Boolean {
        val buildableElement = VlangBuildConfiguration(buildConfiguration, environment)

        invokeLater {
            FileDocumentManager.getInstance().saveAllDocuments()
        }

        val result = CompletableFuture<Boolean>()
        ProjectTaskManager.getInstance(environment.project).build(buildableElement).onProcessed {
            result.complete(!it.hasErrors() && !it.isAborted)
        }
        return result.get()
    }

    abstract class BuildTask<T : BuildTask<T>>(providerId: Key<T>) : BeforeRunTask<T>(providerId) {
        init {
            isEnabled = true
        }
    }
}
