package org.vlang.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.vlang.lang.VlangFileType
import org.vlang.lang.psi.impl.VlangElementFactory.createFileFromText

object VlangElementFactory {
//    fun createFQN(project: Project, name: String): EolangFqn {
//        return EolangFqnImpl(PsiElementFactory.getInstance(project).createIdentifier())
//    }

    fun createFile(project: Project, text: String): VlangFile {
        val name = "dummy.v"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(name, VlangFileType.INSTANCE, text) as VlangFile
    }

    fun createBlock(project: Project): VlangBlock {
        val function = createFileFromText(project, "module a; fn t() {\n}").getFunctions().firstOrNull()
            ?: error("Impossible situation! Parser is broken.")
        return function.getBlock()!!
    }
}
