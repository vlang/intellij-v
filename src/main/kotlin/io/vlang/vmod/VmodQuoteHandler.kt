package io.vlang.vmod

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import io.vlang.vmod.psi.VmodTokenTypes

class VmodQuoteHandler : SimpleTokenSetQuoteHandler(VmodTokenTypes.STRING_LITERALS)
