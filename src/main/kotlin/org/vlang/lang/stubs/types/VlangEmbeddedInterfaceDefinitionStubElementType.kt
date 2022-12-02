package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangEmbeddedInterfaceDefinition
import org.vlang.lang.psi.impl.VlangEmbeddedInterfaceDefinitionImpl
import org.vlang.lang.stubs.VlangEmbeddedInterfaceDefinitionStub

class VlangEmbeddedInterfaceDefinitionStubElementType(name: String) : VlangStubElementType<VlangEmbeddedInterfaceDefinitionStub, VlangEmbeddedInterfaceDefinition>(name) {
    override fun createPsi(stub: VlangEmbeddedInterfaceDefinitionStub): VlangEmbeddedInterfaceDefinition {
        return VlangEmbeddedInterfaceDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangEmbeddedInterfaceDefinition, parentStub: StubElement<*>?): VlangEmbeddedInterfaceDefinitionStub {
        return VlangEmbeddedInterfaceDefinitionStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangEmbeddedInterfaceDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangEmbeddedInterfaceDefinitionStub {
        return VlangEmbeddedInterfaceDefinitionStub(parentStub, this, dataStream.readName())
    }
}
