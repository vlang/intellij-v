package org.vlang.utils

import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentOfTypes
import com.intellij.ui.DocumentAdapter
import org.vlang.lang.VlangFileType
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import kotlin.reflect.KClass
import kotlin.streams.asSequence

val Document.virtualFile: VirtualFile?
    get() = FileDocumentManager.getInstance().getFile(this)

fun Document.psiFile(project: Project) = PsiDocumentManager.getInstance(project).getPsiFile(this)

val VirtualFile.isNotVlangFile: Boolean get() = !isVlangFile
val VirtualFile.isVlangFile: Boolean get() = fileType == VlangFileType
val VirtualFile.isTestFile: Boolean get() = name.endsWith("_test.v")
val VirtualFile.isStubFile: Boolean get() = path.replace("\\", "/").contains("jar!/stubs/")

val VirtualFile.guessProjectForFile get() = ProjectLocator.getInstance().guessProjectForFile(this)

val VirtualFile.pathAsPath: Path get() = Paths.get(path)


fun CapturingProcessHandler.runProcessWithGlobalProgress(timeoutInMilliseconds: Int? = null): ProcessOutput {
    return runProcess(ProgressManager.getGlobalProgressIndicator(), timeoutInMilliseconds)
}

fun String?.indexesOf(string: String): List<Int> =
    string.toRegex()
        .findAll(this?: "")
        .map { it.range.first }
        .toList()

inline fun <reified T: PsiElement> PsiElement.parentNth(depth: Int): T? {
    var parent: PsiElement? = this
    repeat(depth) {
        parent = parent?.parent
    }
    return parent as? T
}

inline fun <reified T: PsiElement> PsiElement.inside(): Boolean {
    return this.parentOfType<T>() != null
}

inline fun <reified T: PsiElement, reified T2: PsiElement> PsiElement.insideAny(): Boolean {
    return parentOfTypes(T::class, T2::class) != null
}

inline fun <reified T : PsiElement> PsiElement.parentOfTypeWithStop(vararg stopAt: KClass<out PsiElement>): T? {
    return PsiTreeUtil.getParentOfType(this, T::class.java, true, *stopAt.map { it.java }.toTypedArray())
}

inline fun <reified T : PsiElement> PsiElement.stubOrPsiParentOfType(): T? {
    return PsiTreeUtil.getStubOrPsiParentOfType(this, T::class.java)
}

inline fun <reified T : PsiElement> PsiElement.ancestorStrict(): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, /* strict */ true)

inline fun <reified T : PsiElement> PsiElement.descendantOfTypeStrict(): T? =
    PsiTreeUtil.findChildOfType(this, T::class.java, /* strict */ true)

inline fun <reified T : PsiElement> PsiElement.childOfType(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

fun PsiElement?.getPrevNonWhitespaceSibling(): PsiElement? =
    PsiTreeUtil.skipWhitespacesBackward(this)

fun CapturingProcessHandler.runProcess(
    indicator: ProgressIndicator?,
    timeoutInMilliseconds: Int? = null,
): ProcessOutput {
    return when {
        indicator != null && timeoutInMilliseconds != null ->
            runProcessWithProgressIndicator(indicator, timeoutInMilliseconds)

        indicator != null -> runProcessWithProgressIndicator(indicator)
        timeoutInMilliseconds != null -> runProcess(timeoutInMilliseconds)
        else -> runProcess()
    }
}

fun PsiElement.line(): Int {
    val document = PsiDocumentManager.getInstance(project).getDocument(containingFile)
    val lineNumber = if (document != null) {
        document.getLineNumber(textRange.startOffset) + 1
    } else {
        0
    }
    return lineNumber
}

inline fun <reified T : PsiElement> PsiElement.ancestorOrSelf(): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java,false)

fun String.toPath(): Path = Paths.get(this)

fun String.toPathOrNull(): Path? = pathOrNull(this::toPath)

fun Path.list(): Sequence<Path> = Files.list(this).asSequence()

private inline fun pathOrNull(block: () -> Path): Path? {
    return try {
        block()
    } catch (e: InvalidPathException) {
        null
    }
}

fun JTextField.addTextChangeListener(listener: (DocumentEvent) -> Unit) {
    document.addDocumentListener(
        object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                listener(e)
            }
        }
    )
}
