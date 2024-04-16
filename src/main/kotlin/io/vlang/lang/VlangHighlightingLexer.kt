package io.vlang.lang

import com.intellij.lexer.LayeredLexer
import io.vlang.lang.lexer.VlangLexer

class VlangHighlightingLexer : LayeredLexer(VlangLexer())
