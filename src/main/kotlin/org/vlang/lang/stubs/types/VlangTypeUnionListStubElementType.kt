package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangTypeUnionList
import org.vlang.lang.psi.impl.VlangTypeUnionListImpl
import org.vlang.lang.stubs.VlangTypeUnionListStub

class VlangTypeUnionListStubElementType(name: String) : VlangStubElementType<VlangTypeUnionListStub, VlangTypeUnionList>(name) {
    override fun createPsi(stub: VlangTypeUnionListStub): VlangTypeUnionList {
        return VlangTypeUnionListImpl(stub, this)
    }

    override fun createStub(psi: VlangTypeUnionList, parentStub: StubElement<*>?): VlangTypeUnionListStub {
        return VlangTypeUnionListStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangTypeUnionListStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangTypeUnionListStub {
        return VlangTypeUnionListStub(parentStub, this, dataStream.readName())
    }
}
