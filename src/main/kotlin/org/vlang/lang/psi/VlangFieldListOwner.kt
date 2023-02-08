package org.vlang.lang.psi

interface VlangFieldListOwner {
    /**
     * list of fields declared in this element and in embedded elements.
     */
    val fieldList: List<VlangFieldDefinition>

    /**
     * list of fields declared in this element.
     */
    val ownFieldList: List<VlangFieldDefinition>
}
