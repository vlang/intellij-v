package org.vlang.ide.editor

import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.IElementType
import org.vlang.lang.psi.VlangTokenTypes

object VlangEditorUtil {
    fun skipWhitespacesForward(iterator: HighlighterIterator): IElementType? {
        while (!iterator.atEnd()) {
            val token = iterator.tokenType
            if (token !== VlangTokenTypes.WS) {
                return token
            }
            iterator.advance()
        }
        return null
    }
}
