package io.vlang.lang.psi

import com.intellij.psi.tree.IElementType
import io.vlang.lang.VlangLanguage

open class VlangTokenType(debugName: String) : IElementType(debugName, VlangLanguage) {
    override fun toString() = "VlangTokenType." + super.toString()
}
