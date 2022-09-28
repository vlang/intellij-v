package org.vlang.ide.documentation

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import org.vlang.lang.psi.VlangConstDefinition
import org.vlang.lang.psi.VlangTokenTypes
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.VlangVarDefinition

object CommentsConverter {
    fun toHtml(comments: List<PsiComment>): String {
        return comments.joinToString("<br>") {
            if (it.tokenType == VlangTokenTypes.LINE_COMMENT) {
                it.text.removePrefix("//")
            } else {
                it.text
                    .removePrefix("/*")
                    .removeSuffix("*/")
                    .lines().joinToString("<br>") {
                        it.removePrefix(" * ")
                    }
            }
        }
    }

    fun getCommentsForElement(element: PsiElement?): List<PsiComment> {
        var comments = getCommentsInner(element)
        if (comments.isEmpty()) {
            if (element is VlangVarDefinition || element is VlangConstDefinition) {
                val parent = element.parent // spec
                comments = getCommentsInner(parent)
                if (comments.isEmpty() && parent != null) {
                    return getCommentsInner(parent.parent) // declaration
                }
            } else if (element is VlangTypeAliasDeclaration) {
                return getCommentsInner(element.parent) // type declaration
            }
        }
        return comments
    }

    private fun getCommentsInner(element: PsiElement?): List<PsiComment> {
        if (element == null) {
            return emptyList()
        }
        val result = mutableListOf<PsiComment>()
        var e: PsiElement?
        e = element.prevSibling
        while (e != null) {
            if (e is PsiWhiteSpace) {
                if (e.getText().contains("\n\n")) return result
                e = e.getPrevSibling()
                continue
            }
            if (e is PsiComment) {
                result.add(0, e)
            } else {
                return result
            }
            e = e.prevSibling
        }
        return result
    }
}
