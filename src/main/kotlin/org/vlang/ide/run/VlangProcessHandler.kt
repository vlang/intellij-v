package org.vlang.ide.run

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.execution.process.AnsiEscapeDecoder
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.openapi.util.Key
import com.intellij.util.io.BaseDataReader
import com.intellij.util.io.BaseOutputReader

class VlangProcessHandler(commandLine: GeneralCommandLine) : KillableProcessHandler(commandLine), AnsiEscapeDecoder.ColoredTextAcceptor {
    private val decoder: AnsiEscapeDecoder?

    init {
        setHasPty(commandLine is PtyCommandLine)
        setShouldDestroyProcessRecursively(!hasPty())
        decoder = if (!hasPty()) AnsiEscapeDecoder() else null
    }

    override fun notifyTextAvailable(text: String, outputType: Key<*>) {
        decoder?.escapeText(text, outputType, this) ?: super.notifyTextAvailable(text, outputType)
    }

    override fun coloredTextAvailable(text: String, attributes: Key<*>) {
        super.notifyTextAvailable(text, attributes)
    }

    override fun readerOptions(): BaseOutputReader.Options = object : BaseOutputReader.Options() {
        override fun policy(): BaseDataReader.SleepingPolicy =
            if (hasPty() || java.lang.Boolean.getBoolean("output.reader.blocking.mode")) {
                BaseDataReader.SleepingPolicy.BLOCKING
            } else {
                BaseDataReader.SleepingPolicy.NON_BLOCKING
            }

        override fun splitToLines(): Boolean = !hasPty()
    }
}
