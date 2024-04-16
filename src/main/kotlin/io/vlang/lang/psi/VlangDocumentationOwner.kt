package io.vlang.lang.psi

import io.vlang.lang.doc.psi.VlangDocComment

interface VlangDocumentationOwner : VlangCompositeElement {
    fun getDocumentation(): VlangDocComment?
}
