package org.vlang.lang

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import org.vlang.lang.psi.VlangTokenTypes

class VlangQuoteHandler : SimpleTokenSetQuoteHandler(VlangTokenTypes.STRING_LITERALS)
