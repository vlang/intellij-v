package org.vlang.ide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import org.vlang.ide.documentation.DocumentationGenerator.generateDoc
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration

class VlangDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        when (element) {
            is VlangFunctionOrMethodDeclaration -> {
                return element.generateDoc()
            }
        }

        return null
    }
}
