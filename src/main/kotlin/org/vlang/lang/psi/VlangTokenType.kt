package org.vlang.lang.psi

import com.intellij.psi.tree.IElementType
import org.vlang.lang.VlangLanguage

class VlangTokenType(debugName: String) : IElementType(debugName, VlangLanguage.INSTANCE) {
    override fun toString() = "VlangTokenType." + super.toString()
}
