package org.vlang.lang.stubs

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.NamedStubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangNamedStub<T : VlangNamedElement> : NamedStubBase<T> {
    val isPublic: Boolean
    val isGlobal: Boolean

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isPublic: Boolean,
        isGlobal: Boolean,
    ) : super(parent, elementType, name) {
        this.isPublic = isPublic
        this.isGlobal = isGlobal
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isPublic: Boolean,
        isGlobal: Boolean,
    ) : super(parent, elementType, name) {
        this.isPublic = isPublic
        this.isGlobal = isGlobal
    }

    override fun toString(): String {
        val name = name
        val str = super.toString()
        return if (StringUtil.isEmpty(name)) str else "$str: $name"
    }
}
