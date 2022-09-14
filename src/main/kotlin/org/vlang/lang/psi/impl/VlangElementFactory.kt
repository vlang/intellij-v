package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangFile

object VlangElementFactory {
    fun createFileFromText(project: Project, text: String): VlangFile {
        return PsiFileFactory.getInstance(project).createFileFromText("dummy.v", VlangLanguage.INSTANCE, text) as VlangFile
    }

    fun createIdentifierFromText(project: Project, text: String): PsiElement {
        val file = createFileFromText(project, "module $text")
        return file.getModule()?.identifier!!
    }
}
