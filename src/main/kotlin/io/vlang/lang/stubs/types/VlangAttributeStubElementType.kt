package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import io.vlang.lang.psi.VlangAttribute
import io.vlang.lang.psi.impl.VlangAttributeImpl
import io.vlang.lang.stubs.VlangAttributeStub

class VlangAttributeStubElementType(name: String) : VlangStubElementType<VlangAttributeStub, VlangAttribute>(name) {
    override fun createPsi(stub: VlangAttributeStub): VlangAttribute {
        return VlangAttributeImpl(stub, this)
    }

    override fun createStub(psi: VlangAttribute, parentStub: StubElement<*>?): VlangAttributeStub {
        return VlangAttributeStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangAttributeStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangAttributeStub {
        return VlangAttributeStub(parentStub, this, dataStream.readName())
    }
}
