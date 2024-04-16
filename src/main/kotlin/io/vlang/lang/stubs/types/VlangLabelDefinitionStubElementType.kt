package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import io.vlang.lang.psi.VlangLabelDefinition
import io.vlang.lang.psi.impl.VlangLabelDefinitionImpl
import io.vlang.lang.stubs.VlangLabelDefinitionStub

class VlangLabelDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangLabelDefinitionStub, VlangLabelDefinition>(name) {
    override fun createPsi(stub: VlangLabelDefinitionStub): VlangLabelDefinition {
        return VlangLabelDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangLabelDefinition, parentStub: StubElement<*>?): VlangLabelDefinitionStub {
        return VlangLabelDefinitionStub(parentStub, this, psi.name)
    }

    override fun serialize(stub: VlangLabelDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangLabelDefinitionStub {
        return VlangLabelDefinitionStub(parentStub, this, dataStream.readName())
    }
}
