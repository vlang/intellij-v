package org.vlang.lang.psi

interface VlangMemberModifiersOwner : VlangCompositeElement {
    val memberModifierList: List<VlangMemberModifier>
}
