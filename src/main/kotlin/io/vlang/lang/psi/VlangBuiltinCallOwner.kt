package io.vlang.lang.psi

import io.vlang.lang.psi.impl.VlangBuiltinReference

interface VlangBuiltinCallOwner {
    fun getReference(): VlangBuiltinReference<*>
}
