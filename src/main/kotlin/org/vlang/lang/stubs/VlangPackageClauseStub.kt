package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangPackageClause

class VlangPackageClauseStub : StubBase<VlangPackageClause> {
    val myName: String?

    constructor(parent: StubElement<*>, type: IStubElementType<*, *>, name: String) : super(parent, type) {
        myName = name
    }

    constructor(stub: StubElement<*>, type: IStubElementType<*, *>, ref: StringRef?) : super(stub, type) {
        myName = ref?.string
    }

    fun getName(): String? {
        return myName
    }
}
