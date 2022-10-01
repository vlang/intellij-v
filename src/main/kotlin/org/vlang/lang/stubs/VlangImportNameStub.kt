package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.NamedStubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangImportName

class VlangImportNameStub : NamedStubBase<VlangImportName> {
    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: StringRef?) :
            super(parent, elementType, name)

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>, name: String?) :
            super(parent, elementType, name)
}
