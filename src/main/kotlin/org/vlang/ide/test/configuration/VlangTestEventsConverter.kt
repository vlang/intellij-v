package org.vlang.ide.test.configuration

import com.intellij.execution.testframework.TestConsoleProperties
import com.intellij.execution.testframework.sm.runner.OutputToGeneralTestEventsConverter
import com.intellij.openapi.util.Key

class VlangTestEventsConverter(consoleProperties: TestConsoleProperties) :
    OutputToGeneralTestEventsConverter(VlangTestConsoleProperties.TEST_FRAMEWORK_NAME, consoleProperties) {

    private var countNewLinesInRow = 0

    override fun process(text: String?, outputType: Key<*>?) {
        if (skipLine(text)) return

        if (text == "\n") {
            countNewLinesInRow++
        } else {
            countNewLinesInRow = 0
        }

        // Skip to many new lines
        if (countNewLinesInRow >= 3) {
            return
        }

        super.process(text, outputType)
    }

    private fun skipLine(text: String?): Boolean {
        if (text == null) return false

        val trimmed = text.trim()
        return when {
            trimmed.startsWith("Summary for") ||
                    trimmed.startsWith("compilation took") ||
                    trimmed.startsWith("generated  target") ||
                    trimmed.startsWith("V  source") ||
                    trimmed.startsWith("FAIL") ||
                    trimmed.startsWith("OK") ||
                    trimmed.startsWith("[@TEST ->") ||
                    RESULT_REGEX.matches(trimmed) -> true

            else                                  -> false
        }
    }

    companion object {
        val RESULT_REGEX = "\\[\\d+/\\d+].*".toRegex()
    }
}
