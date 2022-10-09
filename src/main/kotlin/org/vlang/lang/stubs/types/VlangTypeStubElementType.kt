package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangType
import org.vlang.lang.stubs.VlangTypeStub

abstract class VlangTypeStubElementType(name: String) : VlangStubElementType<VlangTypeStub, VlangType>(name) {
    override fun createStub(psi: VlangType, parentStub: StubElement<*>?): VlangTypeStub {
        return VlangTypeStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangTypeStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangTypeStub {
        return VlangTypeStub(parentStub, this, dataStream.readName())
    }
}
