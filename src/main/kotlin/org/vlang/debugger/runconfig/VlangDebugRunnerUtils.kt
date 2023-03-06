package org.vlang.debugger.runconfig

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.psi.search.ExecutionSearchScopes
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager

object VlangDebugRunnerUtils {
    fun showRunContent(
        environment: ExecutionEnvironment,
        runExecutable: GeneralCommandLine,
    ): RunContentDescriptor {
        val runParameters = VlangDebugRunParameters(
            environment.project,
            runExecutable,
            false,
        )

        val project = environment.project
        val searchScope = ExecutionSearchScopes.executionScope(project, environment.runProfile)
        val consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project, searchScope)

        return XDebuggerManager.getInstance(environment.project)
            .startSession(environment, object : XDebugProcessStarter() {
                override fun start(session: XDebugSession): XDebugProcess =
                    VlangLocalDebugProcess(runParameters, session, consoleBuilder).apply {
                        ProcessTerminatedListener.attach(processHandler, environment.project)
                        start()
                    }
            })
            .runContentDescriptor
    }
}
