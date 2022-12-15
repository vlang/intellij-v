package org.vlang.lang.psi

import org.vlang.lang.psi.impl.VlangBuiltinReference

interface VlangBuiltinCallOwner {
    fun getReference(): VlangBuiltinReference<*>
}
