package io.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import io.vlang.lang.psi.VlangFunctionOrMethodDeclaration

abstract class VlangFunctionOrMethodDeclarationStub<T : VlangFunctionOrMethodDeclaration> : VlangNamedStub<T> {
    protected constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)

    protected constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)
}
