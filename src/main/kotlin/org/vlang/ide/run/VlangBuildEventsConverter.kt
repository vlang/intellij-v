package org.vlang.ide.run

import com.intellij.build.FilePosition
import com.intellij.build.events.BuildEvent
import com.intellij.build.events.MessageEvent
import com.intellij.build.events.impl.FileMessageEventImpl
import com.intellij.build.events.impl.OutputBuildEventImpl
import com.intellij.build.output.BuildOutputInstantReader
import com.intellij.build.output.BuildOutputParser
import com.intellij.execution.process.AnsiEscapeDecoder
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.util.text.StringUtil
import java.io.File
import java.util.function.Consumer

class VlangBuildEventsConverter(private val ctx: VlangBuildContext) : BuildOutputParser {
    private val decoder: AnsiEscapeDecoder = AnsiEscapeDecoder()
    private var cgenError = false

    override fun parse(
        line: String,
        reader: BuildOutputInstantReader,
        messageConsumer: Consumer<in BuildEvent>,
    ): Boolean {
        if (cgenError) {
            messageConsumer.acceptText(ctx.parentId, line.withNewLine())
            return false
        }

        val cleanLine = decoder.removeEscapeSequences(line)
        val kind = getMessageKind(cleanLine)

        if (cleanLine.startsWith("=====")) {
            val event = handleCompilerCgenError()
            messageConsumer.accept(event)
            cgenError = true
        }

        if (kind != MessageEvent.Kind.SIMPLE) {
            val event = handleCompilerMessage(reader, kind, cleanLine, line)
            messageConsumer.accept(event)
        }

        messageConsumer.acceptText(ctx.parentId, cleanLine.withNewLine())
        return true
    }

    private fun getMessageKind(line: String): MessageEvent.Kind = when {
        line.contains("error:")   -> MessageEvent.Kind.ERROR
        line.contains("warning:") -> MessageEvent.Kind.WARNING
        else                      -> MessageEvent.Kind.SIMPLE
    }

    private fun handleCompilerMessage(
        reader: BuildOutputInstantReader,
        kind: MessageEvent.Kind,
        cleanLine: String,
        line: String,
    ): FileMessageEventImpl {
        val message = readWholeMessage(line, reader)

        val about = cleanLine.apply {
            if (kind == MessageEvent.Kind.ERROR) {
                substringAfter("error:")
            } else {
                substringAfter("warning:")
            }
        }.trim().apply {
            val beforeColon = substringBefore(":")
            if (!beforeColon.contains(".")) {
                // if not path
                replaceFirstChar { it.titlecase() }
            }
        }

        if (kind == MessageEvent.Kind.ERROR) {
            ctx.errors.incrementAndGet()
        } else {
            ctx.warnings.incrementAndGet()
        }

        return FileMessageEventImpl(
            ctx.parentId,
            kind,
            VLANG_MESSAGE_GROUP, about, message,
            getFilePosition(cleanLine) ?: FilePosition(ctx.workingDirectory.toFile(), 0, 0)
        )
    }

    private fun readWholeMessage(line: String, reader: BuildOutputInstantReader): String {
        val allMessage = StringBuilder()
        var cleanReadLine: String?
        var readLine: String?
        var readLines = 0

        do {
            readLine = reader.readLine()
            readLines++
            if (readLine == null) {
                break
            }
            cleanReadLine = decoder.removeEscapeSequences(readLine)
            if (cleanReadLine.first().isLetter() && readLines > 2) {
                break
            }

            allMessage.append(readLine + "\n")
        } while (cleanReadLine != null && (cleanReadLine.matches(MESSAGE_LINE) || cleanReadLine.matches(MESSAGE_ERROR_LINE)))

        reader.pushBack(readLines)
        return line + "\n" + allMessage
    }

    private fun handleCompilerCgenError(): FileMessageEventImpl {
        ctx.errors.incrementAndGet()
        return FileMessageEventImpl(
            ctx.parentId,
            MessageEvent.Kind.ERROR,
            VLANG_MESSAGE_GROUP,
            "C gen error",
            "Error while compiling generated C code. See full build log for details",
            FilePosition(ctx.workingDirectory.toFile(), 0, 0)
        )
    }

    private fun getFilePosition(message: String): FilePosition? {
        val parts = message.split(':')
        if (parts.size < 3) {
            return null
        }
        val file = File(ctx.workingDirectory.toFile(), parts[0])
        if (!file.exists()) {
            return null
        }
        val line = parts[1].toInt()
        val column = parts[2].toInt()
        return FilePosition(
            file,
            line - 1,
            column - 1,
            line - 1,
            column - 1
        )
    }

    companion object {
        const val VLANG_MESSAGE_GROUP: String = "V compiler"

        private val MESSAGE_LINE = Regex(".*\\d+.*\\|.*") //  10 | println('hello world')
        private val MESSAGE_ERROR_LINE = Regex(".*\\|.*?~") //   | ~~~~~~~

        private fun Consumer<in BuildEvent>.acceptText(parentId: Any?, text: String) =
            accept(OutputBuildEventImpl(parentId, text, true))

        private fun String.withNewLine(): String = if (StringUtil.endsWithLineBreak(this)) this else this + '\n'

        private fun AnsiEscapeDecoder.removeEscapeSequences(text: String): String {
            val chunks = mutableListOf<String>()
            escapeText(text, ProcessOutputTypes.STDOUT) { chunk, _ ->
                chunks.add(chunk)
            }
            return chunks.joinToString("")
        }
    }
}
