package org.vlang.lang.psi

interface VlangSignatureOwner : VlangCompositeElement {
    fun getSignature(): VlangSignature?
}
