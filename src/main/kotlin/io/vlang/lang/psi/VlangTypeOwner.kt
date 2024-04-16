package io.vlang.lang.psi

import com.intellij.psi.ResolveState
import io.vlang.lang.psi.types.VlangTypeEx

interface VlangTypeOwner : VlangCompositeElement {
    fun getType(context: ResolveState?): VlangTypeEx?
}
