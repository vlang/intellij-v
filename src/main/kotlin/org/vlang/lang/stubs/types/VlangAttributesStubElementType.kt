package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangAttributes
import org.vlang.lang.psi.impl.VlangAttributesImpl
import org.vlang.lang.stubs.VlangAttributesStub

class VlangAttributesStubElementType(name: String) : VlangStubElementType<VlangAttributesStub, VlangAttributes>(name) {
    override fun createPsi(stub: VlangAttributesStub): VlangAttributes {
        return VlangAttributesImpl(stub, this)
    }

    override fun createStub(psi: VlangAttributes, parentStub: StubElement<*>?): VlangAttributesStub {
        return VlangAttributesStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangAttributesStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangAttributesStub {
        return VlangAttributesStub(parentStub, this, dataStream.readName())
    }
}
