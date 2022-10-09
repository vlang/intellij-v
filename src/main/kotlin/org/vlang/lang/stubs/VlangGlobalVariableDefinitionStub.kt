package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangGlobalVariableDefinition

class VlangGlobalVariableDefinitionStub : VlangNamedStub<VlangGlobalVariableDefinition> {
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?, isPublic: Boolean) :
            super(parent, elementType, name, isPublic)
}
