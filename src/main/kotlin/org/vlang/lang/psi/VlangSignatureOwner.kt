package org.vlang.lang.psi

interface VlangSignatureOwner : VlangGenericParametersOwner, VlangCompositeElement {
    fun getSignature(): VlangSignature?
}
