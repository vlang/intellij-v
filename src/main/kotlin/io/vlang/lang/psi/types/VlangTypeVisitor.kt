package io.vlang.lang.psi.types

interface VlangTypeVisitor {
    fun enter(type: VlangTypeEx): Boolean
}
