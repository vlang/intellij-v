package org.vlang.ide.test.configuration

import com.intellij.execution.Location
import com.intellij.execution.PsiLocation
import com.intellij.execution.testframework.sm.runner.SMTestLocator
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangNamedElement
import java.nio.file.Path

object VlangTestLocator : SMTestLocator {
    override fun getLocation(
        protocol: String,
        path: String,
        project: Project,
        scope: GlobalSearchScope,
    ): MutableList<Location<PsiElement>> {
        if (protocol != PROTOCOL_ID) {
            return mutableListOf()
        }

        val (filepath, fqn) = path.split(":")
        val file = VirtualFileManager.getInstance().findFileByNioPath(Path.of(filepath)) ?: return mutableListOf()
        val psiFile = com.intellij.psi.PsiManager.getInstance(project).findFile(file) as? VlangFile ?: return mutableListOf()

        val functionName = fqn.substringAfterLast(".")
        val function = psiFile.getFunctions().find { it.name == functionName } ?: return mutableListOf()

        return mutableListOf(PsiLocation(function))
    }

    fun getTestUrl(element: VlangNamedElement): String {
        val containingFile = element.containingFile
        val qualifiedName = element.getQualifiedName()
        return "$PROTOCOL_ID://${containingFile.virtualFile.path}:$qualifiedName"
    }

    fun getTestUrlWithoutModuleName(element: VlangNamedElement): String {
        val containingFile = element.containingFile
        val name = element.name
        return "$PROTOCOL_ID://${containingFile.virtualFile.path}:$name"
    }

    fun getTestUrlWithoutRootModuleName(element: VlangNamedElement): String {
        val containingFile = element.containingFile
        val qualifiedName = element.getQualifiedName() ?: return ""
        val parts = qualifiedName.split(".")
        if (parts.size > 2) {
            val name = parts.drop(1).joinToString(".")
            return "$PROTOCOL_ID://${containingFile.virtualFile.path}:$name"
        }
        return "$PROTOCOL_ID://${containingFile.virtualFile.path}:${parts.firstOrNull()}"
    }

    private const val PROTOCOL_ID = "v_qn"
}
