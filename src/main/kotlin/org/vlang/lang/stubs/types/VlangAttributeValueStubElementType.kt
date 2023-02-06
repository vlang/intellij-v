package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangAttributeValue
import org.vlang.lang.psi.impl.VlangAttributeValueImpl
import org.vlang.lang.stubs.VlangAttributeValueStub

class VlangAttributeValueStubElementType(name: String) : VlangStubElementType<VlangAttributeValueStub, VlangAttributeValue>(name) {
    override fun createPsi(stub: VlangAttributeValueStub): VlangAttributeValue {
        return VlangAttributeValueImpl(stub, this)
    }

    override fun createStub(psi: VlangAttributeValue, parentStub: StubElement<*>?): VlangAttributeValueStub {
        return VlangAttributeValueStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangAttributeValueStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangAttributeValueStub {
        return VlangAttributeValueStub(parentStub, this, dataStream.readName())
    }
}
