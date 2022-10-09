package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangParamDefinition
import org.vlang.lang.psi.impl.VlangParamDefinitionImpl
import org.vlang.lang.stubs.VlangParamDefinitionStub

class VlangParamDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangParamDefinitionStub, VlangParamDefinition>(name) {
    override fun createPsi(stub: VlangParamDefinitionStub): VlangParamDefinition {
        return VlangParamDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangParamDefinition, parentStub: StubElement<*>?): VlangParamDefinitionStub {
        return VlangParamDefinitionStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangParamDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangParamDefinitionStub {
        return VlangParamDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }
}
