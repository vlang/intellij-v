package org.vlang.lang.psi

import org.vlang.lang.doc.psi.VlangDocComment

interface VlangDocumentationOwner : VlangCompositeElement {
    fun getDocumentation(): VlangDocComment?
}
