package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangImportAlias
import org.vlang.lang.psi.impl.VlangImportAliasImpl
import org.vlang.lang.stubs.VlangImportAliasStub

class VlangImportAliasStubElementType(name: String) : VlangNamedStubElementType<VlangImportAliasStub, VlangImportAlias>(name) {
    override fun createPsi(stub: VlangImportAliasStub): VlangImportAlias {
        return VlangImportAliasImpl(stub, this)
    }

    override fun createStub(psi: VlangImportAlias, parentStub: StubElement<*>?): VlangImportAliasStub {
        return VlangImportAliasStub(parentStub, this, psi.name)
    }

    override fun serialize(stub: VlangImportAliasStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangImportAliasStub {
        return VlangImportAliasStub(parentStub, this, dataStream.readName())
    }
}
