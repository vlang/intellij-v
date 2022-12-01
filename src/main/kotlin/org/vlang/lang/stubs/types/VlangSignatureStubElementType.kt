package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangSignature
import org.vlang.lang.psi.impl.VlangSignatureImpl
import org.vlang.lang.stubs.VlangSignatureStub

class VlangSignatureStubElementType(name: String) : VlangStubElementType<VlangSignatureStub, VlangSignature>(name) {
    override fun createPsi(stub: VlangSignatureStub): VlangSignature {
        return VlangSignatureImpl(stub, this)
    }

    override fun createStub(psi: VlangSignature, parentStub: StubElement<*>?): VlangSignatureStub {
        return VlangSignatureStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangSignatureStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangSignatureStub {
        return VlangSignatureStub(parentStub, this, dataStream.readName())
    }
}
