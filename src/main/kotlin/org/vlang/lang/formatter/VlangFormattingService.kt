package org.vlang.lang.formatter

import com.intellij.codeEditor.printing.HTMLTextPainter
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.lang.psi.VlangFile
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ExecutionException

class VlangFormattingService : AsyncDocumentFormattingService() {
    companion object {
        private val FEATURES = EnumSet.noneOf(FormattingService.Feature::class.java)
    }

    override fun getFeatures(): MutableSet<FormattingService.Feature> = FEATURES

    override fun canFormat(file: PsiFile) = file is VlangFile

    override fun getNotificationGroupId() = "VlangNotifications"

    override fun getName() = "vfmt"

    override fun createFormattingTask(request: AsyncFormattingRequest): FormattingTask? {
        val ioFile = request.ioFile ?: return null
        val params = mutableListOf<String>()
        params.add("fmt")
        params.add(ioFile.path)

        return try {
            val exe = request.context.project.toolchainSettings.toolchain().compiler()
            if (exe == null) {
                request.onError("Can't format", VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)
                return null
            }

            val commandLine = GeneralCommandLine()
                .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
                .withExePath(exe.path)
                .withParameters(params)
            val handler = OSProcessHandler(commandLine.withCharset(StandardCharsets.UTF_8))

            object : FormattingTask {
                override fun run() {
                    handler.addProcessListener(object : CapturingProcessAdapter() {
                        override fun processTerminated(event: ProcessEvent) {
                            val exitCode = event.exitCode
                            if (exitCode == 0) {
                                request.onTextReady(output.stdout)
                            } else {
                                val (offset, error) = formatFmtError(request.context.psiElement, output.stderr)
                                request.onError("Can't format", error, offset)
                            }
                        }
                    })
                    handler.startNotify()
                }

                override fun cancel(): Boolean {
                    handler.destroyProcess()
                    return true
                }

                override fun isRunUnderProgress(): Boolean = true
            }
        } catch (e: ExecutionException) {
            request.onError("Can't format", e.message ?: "")
            null
        }
    }

    private fun formatFmtError(context: PsiElement, error: String): Pair<Int, String> {
        val errorIndex = error.indexOf("error:")
        if (errorIndex == -1) {
            return Pair(0, error)
        }

        val filenameAndLine = error.substring(0, errorIndex).trim()
        val fileParts = filenameAndLine.split(":")
        val line = fileParts.getOrElse(1) { "-1" }.toIntOrNull() ?: -1
        val column = fileParts.getOrElse(2) { "0" }.toIntOrNull() ?: 0
        val firstNewLineIndex = error.indexOf("\n")
        val errorText = error.substring(errorIndex + 6, firstNewLineIndex)

        val flavour = GFMFlavourDescriptor()
        val root = MarkdownParser(flavour).buildMarkdownTreeFromString(errorText)
        val errorTextHtml = HtmlGenerator(errorText, root, flavour)
            .generateHtml()
            .removePrefix("<body>")
            .removeSuffix("</body>")

        val file = context.containingFile
        val document = PsiDocumentManager.getInstance(context.project).getDocument(file)
        val lineOffset = document?.getLineStartOffset(line - 1) ?: -1
        val offset = lineOffset + column

        var internalVfmtIndex = error.indexOf("Internal vfmt")
        if (internalVfmtIndex == -1) {
            internalVfmtIndex = error.lastIndex
        }

        val codeBlock = error.substring(firstNewLineIndex, internalVfmtIndex).trimEnd()
        val codeHtml = HTMLTextPainter.convertCodeFragmentToHTMLFragmentWithInlineStyles(context, codeBlock)
        val errorHtml = errorTextHtml + codeHtml

        return offset to errorHtml
    }
}
