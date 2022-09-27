package org.vlang.ide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import org.vlang.ide.documentation.DocumentationGenerator.generateDoc
import org.vlang.lang.psi.*

class VlangDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        when (element) {
            is VlangFunctionOrMethodDeclaration -> return element.generateDoc()
            is VlangStructDeclaration           -> return element.generateDoc()
            is VlangInterfaceDeclaration        -> return element.generateDoc()
            is VlangEnumDeclaration             -> return element.generateDoc()
            is VlangUnionDeclaration            -> return element.generateDoc()
            is VlangConstDefinition             -> return element.generateDoc()
            is VlangVarDefinition               -> return element.generateDoc()
            is VlangReceiver                    -> return element.generateDoc()
        }

        return null
    }
}
