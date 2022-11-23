package org.vlang.lang.stubs

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.psi.VlangModuleClause

class VlangModuleClauseStub : VlangNamedStub<VlangModuleClause> {
    val qualifiedName: String

    constructor(parent: StubElement<*>, type: IStubElementType<*, *>, name: String, qualifiedName: String)
            : super(parent, type, name, true) {
        this.qualifiedName = qualifiedName
    }

    constructor(stub: StubElement<*>, type: IStubElementType<*, *>, ref: StringRef?, qualifiedName: StringRef?)
            : super(stub, type, ref, true) {
        this.qualifiedName = qualifiedName?.string ?: ""
    }
}
