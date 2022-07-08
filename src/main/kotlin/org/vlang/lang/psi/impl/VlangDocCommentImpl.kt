package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangDocComment

class VlangDocCommentImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangDocComment {
    override fun getTokenType() = node.elementType

    override fun getOwner(): PsiElement? {
        // todo
        return null
    }
}
