package org.vlang.ide.test.configuration

import com.intellij.execution.Executor
import com.intellij.execution.testframework.TestConsoleProperties
import com.intellij.execution.testframework.sm.SMCustomMessagesParsing
import com.intellij.execution.testframework.sm.runner.OutputToGeneralTestEventsConverter
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.testframework.sm.runner.SMTestLocator

class VlangTestConsoleProperties(config: VlangTestConfiguration, executor: Executor) :
    SMTRunnerConsoleProperties(config, "VlangTest", executor),
    SMCustomMessagesParsing {

    internal class VlangTestEventsConverter(consoleProperties: TestConsoleProperties) :
        OutputToGeneralTestEventsConverter("VlangTest", consoleProperties)

    override fun createTestEventsConverter(
        testFrameworkName: String,
        consoleProperties: TestConsoleProperties
    ): OutputToGeneralTestEventsConverter {
        return VlangTestEventsConverter(consoleProperties)
    }

//    override fun createRerunFailedTestsAction(consoleView: ConsoleView?) =
//        VlangTestRerunFailedTestsAction(consoleView!!, this)

    /**
     * @see com.vk.admstorm.configuration.kbench.KBenchConsoleProperties.getTestLocator
     */
    override fun getTestLocator() = SMTestLocator { protocol, path, project, scope ->
        emptyList()
//        provider.getLocation(protocol, path, project, scope)
    }
}
