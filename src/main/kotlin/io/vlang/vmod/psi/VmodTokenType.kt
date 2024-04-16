package io.vlang.vmod.psi

import com.intellij.psi.tree.IElementType
import io.vlang.vmod.VmodLanguage

class VmodTokenType(debugName: String) : IElementType(debugName, VmodLanguage.INSTANCE) {
    override fun toString() = "VmodTokenType." + super.toString()
}
