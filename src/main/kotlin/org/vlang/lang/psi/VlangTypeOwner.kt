package org.vlang.lang.psi

import com.intellij.psi.ResolveState
import org.vlang.lang.psi.types.VlangTypeEx

interface VlangTypeOwner : VlangCompositeElement {
    fun getType(context: ResolveState?): VlangTypeEx?
}
