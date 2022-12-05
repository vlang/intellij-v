package org.vlang.debugger.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import org.vlang.debugger.runconfig.CidrToolEnvironment
import java.io.File
import java.util.concurrent.ExecutionException

class VlangDownloadedLldbToolEnvironment(private val project: Project) : CidrToolEnvironment() {

    private val binaries: VlangLldbDownloadService.LLDBStatus.Binaries by lazy {
        if (!checkToolchainConfigured(project)) {
            throw ExecutionException("LLDB debugger is unavailable.", null)
        }
        VlangLldbDownloadService.getInstance().getLLDBStatus() as VlangLldbDownloadService.LLDBStatus.Binaries
    }

    val lldbFrontendFile: File get() = binaries.frontendFile
    val lldbFrameworkFile: File get() = binaries.frameworkFile

    companion object {

        fun checkToolchainConfigured(project: Project): Boolean {
            val lldbStatus = VlangLldbDownloadService.getInstance().getLLDBStatus()
            val (message, action) = when (lldbStatus) {
                VlangLldbDownloadService.LLDBStatus.Unavailable -> return false
                VlangLldbDownloadService.LLDBStatus.NeedToDownload -> "LLDB debugger binaries must be downloaded." to "Download"
                VlangLldbDownloadService.LLDBStatus.NeedToUpdate -> "LLDB debugger binaries are outdated." to "Update"
                is VlangLldbDownloadService.LLDBStatus.Binaries -> return true
            }

            val option = if (!VlangDebuggerState.getInstance().downloadAutomatically) {
                showDialog(project, message, action)
            } else {
                Messages.OK
            }

            if (option == Messages.OK) {
                val result = VlangLldbDownloadService.getInstance().downloadDebugger(project)
                if (result is VlangLldbDownloadService.DownloadResult.Ok) {
                    VlangDebuggerState.getInstance().lldbPath = result.lldbDir.absolutePath
                    return true
                }
            }
            return false
        }

        private fun showDialog(project: Project, message: String, action: String): Int {
            return Messages.showDialog(
                project,
                message,
                "Unable to Start Debugger",
                arrayOf(action, Messages.getCancelButton()),
                Messages.OK,
                Messages.getErrorIcon(),
                object : DialogWrapper.DoNotAskOption.Adapter() {
                    override fun rememberChoice(isSelected: Boolean, exitCode: Int) {
                        if (exitCode == Messages.OK) {
                            VlangDebuggerState.getInstance().downloadAutomatically = isSelected
                        }
                    }
                }
            )
        }
    }
}
