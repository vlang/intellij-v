package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubBase
import io.vlang.lang.VlangLanguage
import io.vlang.lang.psi.VlangCompositeElement

abstract class VlangStubElementType<S : StubBase<T>, T : VlangCompositeElement>(debugName: String) :
    IStubElementType<S, T>(debugName, VlangLanguage) {

    override fun getExternalId() = "vlang." + super.toString()

    override fun indexStub(stub: S, sink: IndexSink) {}
}
