package org.vlang.lang.annotator

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object VlangHighlightingData {
    val VLANG_LABEL =
        TextAttributesKey.createTextAttributesKey("VLANG_LABEL", DefaultLanguageHighlighterColors.LABEL)
}
