package org.vlang.ide.documentation

import com.intellij.psi.PsiComment
import org.vlang.lang.psi.VlangTokenTypes

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
}
