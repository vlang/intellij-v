package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.impl.VlangFieldDefinitionImpl
import org.vlang.lang.stubs.VlangFieldDefinitionStub
import org.vlang.lang.stubs.index.VlangFieldFingerprintIndex
import org.vlang.lang.stubs.index.VlangFieldIndex

class VlangFieldDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangFieldDefinitionStub, VlangFieldDefinition>(name) {
    override fun createPsi(stub: VlangFieldDefinitionStub): VlangFieldDefinition {
        return VlangFieldDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangFieldDefinition, parentStub: StubElement<*>?): VlangFieldDefinitionStub {
        return VlangFieldDefinitionStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangFieldDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangFieldDefinitionStub {
        return VlangFieldDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }

    override fun indexStub(stub: VlangFieldDefinitionStub, sink: IndexSink) {
        super.indexStub(stub, sink)

        val name = stub.name
        if (name != null) {
            sink.occurrence(VlangFieldFingerprintIndex.KEY, name)
            sink.occurrence(VlangFieldIndex.KEY, name)
        }
    }
}
