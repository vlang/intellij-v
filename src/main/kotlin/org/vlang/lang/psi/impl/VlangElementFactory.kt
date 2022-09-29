package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiParserFacade
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangImportDeclaration
import org.vlang.lang.psi.VlangImportList

object VlangElementFactory {
    fun createFileFromText(project: Project, text: String): VlangFile {
        return PsiFileFactory.getInstance(project).createFileFromText("dummy.v", VlangLanguage.INSTANCE, text) as VlangFile
    }

    fun createIdentifierFromText(project: Project, text: String): PsiElement {
        val file = createFileFromText(project, "module $text")
        return file.getModule()?.identifier!!
    }

    fun createImportDeclaration(project: Project, name: String, alias: String?): VlangImportDeclaration? {
        return createImportList(project, name, alias)?.importDeclarationList?.firstOrNull()
    }

    fun createNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n")
    }

    fun createDoubleNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n\n")
    }

    fun createImportList(project: Project, name: String, alias: String?): VlangImportList? {
        val text = if (alias != null) "import $name as $alias" else "import $name"
        val file = createFileFromText(project, text)
        return file.getImportList()
    }
}
