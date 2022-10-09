package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangReceiver
import org.vlang.lang.psi.impl.VlangReceiverImpl
import org.vlang.lang.stubs.VlangReceiverStub

class VlangReceiverStubElementType(name: String) : VlangNamedStubElementType<VlangReceiverStub, VlangReceiver>(name) {
    override fun createPsi(stub: VlangReceiverStub): VlangReceiver {
        return VlangReceiverImpl(stub, this)
    }

    override fun createStub(psi: VlangReceiver, parentStub: StubElement<*>?): VlangReceiverStub {
        return VlangReceiverStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangReceiverStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangReceiverStub {
        return VlangReceiverStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }
}
