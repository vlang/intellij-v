package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.psi.impl.VlangImportNameImpl
import org.vlang.lang.stubs.VlangImportNameStub

class VlangImportNameStubElementType(name: String) : VlangStubElementType<VlangImportNameStub, VlangImportName>(name) {
    override fun createPsi(stub: VlangImportNameStub): VlangImportName {
        return VlangImportNameImpl(stub, this)
    }

    override fun createStub(psi: VlangImportName, parentStub: StubElement<*>?): VlangImportNameStub {
        return VlangImportNameStub(parentStub, this, psi.name)
    }

    override fun serialize(stub: VlangImportNameStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangImportNameStub {
        return VlangImportNameStub(parentStub, this, dataStream.readName())
    }
}
