package org.vlang.ide.run

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.util.Key

class VlangBuildTaskProvider : VlangBuildTaskBaseProvider<VlangBuildTaskProvider.BuildTask>() {
    override fun getId(): Key<BuildTask> = ID

    override fun createTask(runConfiguration: RunConfiguration): BuildTask? =
        if (runConfiguration is VlangRunConfiguration) BuildTask() else null

    override fun executeTask(
        context: DataContext,
        configuration: RunConfiguration,
        environment: ExecutionEnvironment,
        task: BuildTask
    ): Boolean {
        if (configuration !is VlangRunConfiguration) return false
        return doExecuteTask(configuration, environment)
    }

    class BuildTask : VlangBuildTaskBaseProvider.BuildTask<BuildTask>(ID)

    companion object {
        @JvmField
        val ID: Key<BuildTask> = Key.create("VLANG.BUILD_TASK_PROVIDER")
    }
}
