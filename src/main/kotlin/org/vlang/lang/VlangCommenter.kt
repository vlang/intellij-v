package org.vlang.lang

import com.intellij.lang.CodeDocumentationAwareCommenterEx
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangDocComment
import org.vlang.lang.psi.VlangDocTokenTypes
import org.vlang.lang.psi.VlangTokenTypes

class VlangCommenter : CodeDocumentationAwareCommenterEx {
    override fun getLineCommentPrefix() = "//"

    override fun getBlockCommentPrefix() = "/*"

    override fun getBlockCommentSuffix() = "*/"

    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null

    override fun getLineCommentTokenType() = VlangTokenTypes.LINE_COMMENT

    override fun getBlockCommentTokenType() = VlangTokenTypes.MULTILINE_COMMENT

    override fun getDocumentationCommentTokenType() = VlangDocTokenTypes.DOC_COMMENT

    override fun getDocumentationCommentPrefix() = "/**"

    override fun getDocumentationCommentLinePrefix() = "*"

    override fun getDocumentationCommentSuffix() = "*/"

    override fun isDocumentationComment(element: PsiComment) = element is VlangDocComment

    override fun isDocumentationCommentText(element: PsiElement): Boolean {
        val node = element.node ?: return false
        return node.elementType === VlangDocTokenTypes.DOC_COMMENT_TAG ||
                node.elementType === VlangDocTokenTypes.DOC_COMMENT_BODY
    }
}
