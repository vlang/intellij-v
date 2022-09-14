package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangLabelDefinition
import org.vlang.lang.psi.impl.VlangLabelDefinitionImpl
import org.vlang.lang.stubs.VlangLabelDefinitionStub

class VlangLabelDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangLabelDefinitionStub, VlangLabelDefinition>(name) {
    override fun createPsi(stub: VlangLabelDefinitionStub): VlangLabelDefinition {
        return VlangLabelDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangLabelDefinition, parentStub: StubElement<*>?): VlangLabelDefinitionStub {
        return VlangLabelDefinitionStub(parentStub, this, psi.getName(), psi.isPublic(), psi.isGlobal())
    }

    override fun serialize(stub: VlangLabelDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getName())
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return false // todo
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangLabelDefinitionStub {
        return VlangLabelDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }
}
