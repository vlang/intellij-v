package org.vlang.lang.doc.psi

import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType

class VlangDocCompositeTokenType(
    debugName: String,
    private val astFactory: (IElementType) -> CompositeElement
) : VlangDocTokenType(debugName), ICompositeElementType {
    override fun createCompositeNode(): CompositeElement = astFactory(this)
}
