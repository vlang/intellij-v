package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangMethodDeclaration

class VlangMethodDeclarationStub : VlangFunctionOrMethodDeclarationStub<VlangMethodDeclaration> {
    private val myTypeName: StringRef?

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: StringRef?,
        isPublic: Boolean,
        isGlobal: Boolean,
        typeName: StringRef?
    ) : super(parent, elementType, name, isPublic, isGlobal) {
        myTypeName = typeName
    }

    constructor(
        parent: StubElement<*>?,
        elementType: IStubElementType<*, *>,
        name: String?,
        isPublic: Boolean,
        isGlobal: Boolean,
        typeName: String?
    ) : super(parent, elementType, name, isPublic, isGlobal) {
        myTypeName = StringRef.fromString(typeName)
    }

    val typeName: String?
        get() = myTypeName?.string
}
