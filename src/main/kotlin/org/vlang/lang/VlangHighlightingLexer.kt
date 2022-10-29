package org.vlang.lang

import com.intellij.lexer.LayeredLexer
import org.vlang.lang.lexer.VlangLexer

class VlangHighlightingLexer : LayeredLexer(VlangLexer())
