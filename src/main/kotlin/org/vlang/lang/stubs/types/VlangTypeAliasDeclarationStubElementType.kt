package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.impl.VlangTypeAliasDeclarationImpl
import org.vlang.lang.stubs.VlangTypeAliasDeclarationStub

class VlangTypeAliasDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangTypeAliasDeclarationStub, VlangTypeAliasDeclaration>(name) {

    override fun createPsi(stub: VlangTypeAliasDeclarationStub): VlangTypeAliasDeclaration {
        return VlangTypeAliasDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangTypeAliasDeclaration, parentStub: StubElement<*>): VlangTypeAliasDeclarationStub {
        return VlangTypeAliasDeclarationStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangTypeAliasDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>) =
        VlangTypeAliasDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
}
