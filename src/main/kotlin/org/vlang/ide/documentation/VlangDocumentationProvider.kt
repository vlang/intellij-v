package org.vlang.ide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import org.vlang.ide.documentation.DocumentationGenerator.generateDoc
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangModule.VlangPomTargetPsiElement
import java.util.function.Consumer

class VlangDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?) = when (element) {
        is VlangFunctionOrMethodDeclaration -> element.generateDoc()
        is VlangStructDeclaration           -> element.generateDoc(originalElement)
        is VlangInterfaceDeclaration        -> element.generateDoc(originalElement)
        is VlangEnumDeclaration             -> element.generateDoc(originalElement)
        is VlangConstDefinition             -> element.generateDoc()
        is VlangVarDefinition               -> element.generateDoc(originalElement)
        is VlangParamDefinition             -> element.generateDoc(originalElement)
        is VlangGlobalVariableDefinition    -> element.generateDoc()
        is VlangFieldDefinition             -> element.generateDoc()
        is VlangInterfaceMethodDefinition   -> element.generateDoc()
        is VlangReceiver                    -> element.generateDoc()
        is VlangEnumFieldDefinition         -> element.generateDoc()
        is VlangGenericParameter            -> element.generateDoc()
        is VlangTypeAliasDeclaration        -> element.generateDoc(originalElement)
        is VlangModuleClause                -> element.generateDoc()
        is VlangPomTargetPsiElement         -> element.generateDoc()
        is VlangEmbeddedDefinition          -> element.generateDoc()
        else                                -> null
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        if (file !is VlangFile) return
        for (element in SyntaxTraverser.psiTraverser(file)) {
            if (element is VlangDocComment && element.owner != null) {
                sink.accept(element)
            }
        }
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        return (comment as? VlangDocComment)?.documentationAsHtml(renderMode = VlangDocRenderMode.INLINE_DOC_COMMENT)
    }
}
