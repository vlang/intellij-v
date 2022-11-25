package org.vlang.lang.psi

interface VlangVisibilityOwner : VlangCompositeElement {
    fun isPublic(): Boolean
    fun makePublic()
    fun makePrivate()
    fun getSymbolVisibility(): VlangSymbolVisibility?
}
