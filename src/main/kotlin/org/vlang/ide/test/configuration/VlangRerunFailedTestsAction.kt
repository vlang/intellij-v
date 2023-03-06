package org.vlang.ide.test.configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.testframework.actions.AbstractRerunFailedTestsAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComponentContainer

class VlangRerunFailedTestsAction(container: ComponentContainer, props: VlangTestConsoleProperties) :
    AbstractRerunFailedTestsAction(container) {

    init {
        init(props)
    }

    override fun getRunProfile(environment: ExecutionEnvironment): MyRunProfile {
        val configuration = myConsoleProperties.configuration as VlangTestConfiguration
        return RemotePhpUnitRerunProfile(configuration, getFailedTestPatterns(configuration.project))
    }

    private fun getFailedTestPatterns(project: Project): List<String> {
        val result = mutableSetOf<String>()
        val failedTests = getFailedTests(project)

        for (failedTest in failedTests) {
            result.add(failedTest.name)
        }

        return result.toList()
    }

    private class RemotePhpUnitRerunProfile(
        private val conf: VlangTestConfiguration,
        private val failed: List<String>
    ) : MyRunProfile(conf) {

        override fun getState(exec: Executor, env: ExecutionEnvironment): RunProfileState {
            conf.pattern = failed.joinToString("|")
            return VlangTestConfigurationRunState(env, conf)
        }
    }
}
