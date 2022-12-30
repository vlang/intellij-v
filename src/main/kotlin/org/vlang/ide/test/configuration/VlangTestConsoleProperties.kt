package org.vlang.ide.test.configuration

import com.intellij.execution.Executor
import com.intellij.execution.testframework.Printer
import com.intellij.execution.testframework.TestConsoleProperties
import com.intellij.execution.testframework.sm.SMCustomMessagesParsing
import com.intellij.execution.testframework.sm.SMStacktraceParser
import com.intellij.execution.testframework.sm.runner.OutputToGeneralTestEventsConverter
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.execution.testframework.sm.runner.ui.TestStackTraceParser
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.project.Project

class VlangTestConsoleProperties(config: VlangTestConfiguration, executor: Executor) :
    SMTRunnerConsoleProperties(config, TEST_FRAMEWORK_NAME, executor),
    SMCustomMessagesParsing,
    SMStacktraceParser {

    override fun createTestEventsConverter(
        testFrameworkName: String,
        consoleProperties: TestConsoleProperties,
    ): OutputToGeneralTestEventsConverter = VlangTestEventsConverter(consoleProperties)

    override fun createRerunFailedTestsAction(consoleView: ConsoleView) = VlangRerunFailedTestsAction(consoleView, this)

    override fun getTestLocator() = VlangTestLocator

    override fun getTestStackTraceParser(url: String, proxy: SMTestProxy, project: Project): TestStackTraceParser {
        val stacktrace = proxy.stacktrace
        val failedLine = stacktrace?.substringAfterLast(":")?.toIntOrNull() ?: -1
        return object : TestStackTraceParser(failedLine, null, proxy.errorMessage, stacktrace) {}
    }

    override fun printExpectedActualHeader(printer: Printer, expected: String, actual: String) {
        printer.print("\n", ConsoleViewContentType.ERROR_OUTPUT)
        printer.print("Left:  ", ConsoleViewContentType.SYSTEM_OUTPUT)
        printer.print("$actual\n", ConsoleViewContentType.ERROR_OUTPUT)
        printer.print("Right: ", ConsoleViewContentType.SYSTEM_OUTPUT)
        printer.print(expected, ConsoleViewContentType.ERROR_OUTPUT)
    }

    companion object {
        const val TEST_FRAMEWORK_NAME: String = "V Test"
    }
}
