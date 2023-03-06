package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangResult

class VlangResultStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, ref: StringRef?) :
    StubWithText<VlangResult>(parent, elementType, ref) {

    constructor(parent: StubElement<*>?, elementType: IStubElementType<*, *>?, text: String?) :
            this(parent, elementType, StringRef.fromString(text))
}
