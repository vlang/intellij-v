package org.vlang.utils

import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import org.vlang.lang.VlangFileType

val Document.virtualFile: VirtualFile?
    get() = FileDocumentManager.getInstance().getFile(this)

val VirtualFile.isNotVlangFile: Boolean get() = !isVlangFile
val VirtualFile.isVlangFile: Boolean get() = fileType == VlangFileType.INSTANCE

val VirtualFile.guessProjectForFile get() = ProjectLocator.getInstance().guessProjectForFile(this)

fun CapturingProcessHandler.runProcessWithGlobalProgress(timeoutInMilliseconds: Int? = null): ProcessOutput {
    return runProcess(ProgressManager.getGlobalProgressIndicator(), timeoutInMilliseconds)
}

inline fun <reified T: PsiElement> PsiElement.parentNum(depth: Int): T? {
    var parent: PsiElement? = this
    repeat(depth) {
        parent = parent?.parent
    }
    return parent as? T
}

fun CapturingProcessHandler.runProcess(
    indicator: ProgressIndicator?,
    timeoutInMilliseconds: Int? = null
): ProcessOutput {
    return when {
        indicator != null && timeoutInMilliseconds != null ->
            runProcessWithProgressIndicator(indicator, timeoutInMilliseconds)

        indicator != null -> runProcessWithProgressIndicator(indicator)
        timeoutInMilliseconds != null -> runProcess(timeoutInMilliseconds)
        else -> runProcess()
    }
}
