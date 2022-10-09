package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangInterfaceMethodDefinition
import org.vlang.lang.psi.impl.VlangInterfaceMethodDefinitionImpl
import org.vlang.lang.stubs.VlangInterfaceMethodDefinitionStub

class VlangInterfaceMethodDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangInterfaceMethodDefinitionStub, VlangInterfaceMethodDefinition>(name) {
    override fun createPsi(stub: VlangInterfaceMethodDefinitionStub): VlangInterfaceMethodDefinition {
        return VlangInterfaceMethodDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangInterfaceMethodDefinition, parentStub: StubElement<*>?): VlangInterfaceMethodDefinitionStub {
        return VlangInterfaceMethodDefinitionStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangInterfaceMethodDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangInterfaceMethodDefinitionStub {
        return VlangInterfaceMethodDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }
}
