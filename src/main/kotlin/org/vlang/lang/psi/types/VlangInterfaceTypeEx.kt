package org.vlang.lang.psi.types

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangInterfaceType

class VlangInterfaceTypeEx(raw: VlangInterfaceType): VlangBaseTypeEx<VlangInterfaceType>(raw), VlangImportableType {
    private val decl = raw.parent as VlangInterfaceDeclaration
    private val name = decl.getQualifiedName() ?: ANON

    override fun toString() = name

    override fun qualifiedName() = name

    override fun readableName(context: VlangCompositeElement) = name

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }
}
