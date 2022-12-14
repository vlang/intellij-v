package org.vlang.lang.psi

import com.intellij.psi.tree.IElementType
import org.vlang.lang.VlangLanguage

open class VlangTokenType(debugName: String) : IElementType(debugName, VlangLanguage) {
    override fun toString() = "VlangTokenType." + super.toString()
}
