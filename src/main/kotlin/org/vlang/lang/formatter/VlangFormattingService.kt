package org.vlang.lang.formatter

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.formatting.service.AsyncDocumentFormattingService
import com.intellij.formatting.service.AsyncFormattingRequest
import com.intellij.formatting.service.FormattingService
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangFile
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
            val commandLine = GeneralCommandLine()
                .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
                .withExePath("v")
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
                                request.onError("Can't format", output.stderr)
                            }
                        }
                    })
                    handler.startNotify()
                }

                override fun cancel(): Boolean {
                    handler.destroyProcess()
                    return true
                }

                override fun isRunUnderProgress(): Boolean {
                    return true
                }
            }
        } catch (e: ExecutionException) {
            request.onError("Can't format", e.message ?: "")
            null
        }
    }
}
