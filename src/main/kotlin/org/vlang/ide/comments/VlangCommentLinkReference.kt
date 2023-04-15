package org.vlang.ide.comments

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.vlang.lang.doc.psi.VlangDocComment

class VlangCommentLinkReference(element: PsiComment, rangeInElement: TextRange) : VlangCommentReference(element, rangeInElement) {
    override fun resolve(): PsiElement? {
        val comment = element as? VlangDocComment ?: return null
        return comment.owner
    }
}
