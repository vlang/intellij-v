package org.vlang.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.vlang.lang.VlangFileType

object VlangElementFactory {
//    fun createFQN(project: Project, name: String): EolangFqn {
//        return EolangFqnImpl(PsiElementFactory.getInstance(project).createIdentifier())
//    }

    fun createFile(project: Project, text: String): VlangFile {
        val name = "dummy.v"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(name, VlangFileType.INSTANCE, text) as VlangFile
    }
}