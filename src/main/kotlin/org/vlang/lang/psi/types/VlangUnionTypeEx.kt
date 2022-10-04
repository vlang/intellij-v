package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangUnionDeclaration
import org.vlang.lang.psi.VlangUnionType

class VlangUnionTypeEx(raw: VlangUnionType): VlangBaseTypeEx<VlangUnionType>(raw), VlangImportableType {
    private val decl = raw.parent as VlangUnionDeclaration
    private val name = decl.getQualifiedName() ?: ANON

    override fun toString() = name

    override fun qualifiedName() = name

    override fun readableName(context: VlangCompositeElement) = name

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }
}
