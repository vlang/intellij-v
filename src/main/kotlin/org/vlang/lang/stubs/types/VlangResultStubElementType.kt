package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangResult
import org.vlang.lang.psi.impl.VlangResultImpl
import org.vlang.lang.stubs.VlangResultStub

class VlangResultStubElementType(name: String) : VlangStubElementType<VlangResultStub, VlangResult>(name) {
    override fun createPsi(stub: VlangResultStub): VlangResult {
        return VlangResultImpl(stub, this)
    }

    override fun createStub(psi: VlangResult, parentStub: StubElement<*>?): VlangResultStub {
        return VlangResultStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangResultStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangResultStub {
        return VlangResultStub(parentStub, this, dataStream.readName())
    }
}
