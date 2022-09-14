package org.vlang.lang.psi

import com.intellij.psi.ResolveState

interface VlangTypeOwner : VlangCompositeElement {
    fun getType(context: ResolveState?): VlangType?
}
