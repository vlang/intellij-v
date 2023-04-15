package org.vlang.lang.psi.impl

import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessProvider
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.FileIndexFacade
import com.intellij.pom.PomRenameableTarget
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.impl.PomTargetPsiElementImpl
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.CommonProcessors
import com.intellij.util.Processor
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangFile
import org.vlang.utils.isNotVlangFile

class VlangModule(
    private val project: Project,
    private val name: String,
    val directory: PsiDirectory,
) : PomRenameableTarget<VlangModule> {

    private var isValid = true

    override fun navigate(requestFocus: Boolean) {
        directory.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = directory.canNavigate()

    override fun canNavigateToSource(): Boolean = false

    override fun getName(): String = name

    override fun isWritable(): Boolean {
        val processor: CommonProcessors.FindFirstProcessor<PsiFile> =
            object : CommonProcessors.FindFirstProcessor<PsiFile>() {
                override fun accept(file: PsiFile): Boolean {
                    return !NonProjectFileWritingAccessProvider.isWriteAccessAllowed(file.virtualFile, file.project)
                }
            }
        processFiles(processor)
        return !processor.isFound
    }

    fun toPsi(): VlangPomTargetPsiElement = VlangPomTargetPsiElement(project, this)

    private fun processFiles(processor: Processor<in PsiFile>) {
        if (!isValid()) {
            return
        }

        val fileIndexFacade = FileIndexFacade.getInstance(project)
        for (psiFile in directory.children) {
            if (psiFile !is PsiFile) continue
            val virtualFile = psiFile.virtualFile ?: continue
            ProgressIndicatorProvider.checkCanceled()

            if (virtualFile.isDirectory || !virtualFile.isValid) continue
            if (virtualFile.isNotVlangFile) continue
            if (fileIndexFacade.isExcludedFile(virtualFile)) continue

            if (!processor.process(psiFile)) return
        }
    }

    override fun setName(newName: String): VlangModule {
        if (!isValid() || name == newName) {
            return this
        }

        processFiles { file ->
            if (file !is VlangFile) return@processFiles true

            val moduleClause = file.getModule()
            if (moduleClause != null) {
                val newModuleClause = VlangElementFactory.createModuleClause(project, newName)
                moduleClause.replace(newModuleClause)
            }

            true
        }

        directory.name = newName
        isValid = false

        return this
    }

    override fun isValid(): Boolean {
        if (!isValid || project.isDisposed) return false
        return directory.isValid
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangModule

        if (name != other.name) return false
        return directory == other.directory
    }

    override fun hashCode(): Int {
        var result = directory.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    companion object {
        fun fromDirectory(directory: PsiDirectory): VlangModule {
            var name = directory.name
            for (psiFile in directory.children) {
                if (psiFile !is VlangFile) continue
                name = psiFile.getModuleQualifiedName()
                break
            }

            return VlangModule(directory.project, name, directory)
        }
    }

    class VlangPomTargetPsiElement(project: Project, target: VlangModule) : PomTargetPsiElementImpl(project, target), PsiNameIdentifierOwner {
        override fun getLanguage() = VlangLanguage
        override fun getNameIdentifier() = null
        override fun getTarget() = super.getTarget() as VlangModule
        override fun getUseScope() = GlobalSearchScope.allScope(project)
        override fun toString() = "Psi Wrapper[$target]"
    }
}
