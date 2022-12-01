package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangStructDeclaration

class VlangStructDeclarationStub : VlangNamedStub<VlangStructDeclaration> {
    var isUnion = false

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?, isUnion: Boolean, isPublic: Boolean) :
            super(parent, elementType, name, isPublic) {
        this.isUnion = isUnion
    }

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?, isUnion: Boolean, isPublic: Boolean) :
            super(parent, elementType, name, isPublic) {
        this.isUnion = isUnion
    }
}
