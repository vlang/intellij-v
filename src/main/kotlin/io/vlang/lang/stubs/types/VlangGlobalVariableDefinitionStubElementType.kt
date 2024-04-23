package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import io.vlang.lang.psi.VlangGlobalVariableDefinition
import io.vlang.lang.psi.impl.VlangGlobalVariableDefinitionImpl
import io.vlang.lang.stubs.VlangGlobalVariableDefinitionStub
import io.vlang.lang.stubs.index.VlangGlobalVariablesIndex

class VlangGlobalVariableDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangGlobalVariableDefinitionStub, VlangGlobalVariableDefinition>(name) {
    override fun createPsi(stub: VlangGlobalVariableDefinitionStub): VlangGlobalVariableDefinition {
        return VlangGlobalVariableDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangGlobalVariableDefinition, parentStub: StubElement<*>?): VlangGlobalVariableDefinitionStub {
        return VlangGlobalVariableDefinitionStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangGlobalVariableDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangGlobalVariableDefinitionStub {
        return VlangGlobalVariableDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS = listOf(VlangGlobalVariablesIndex.KEY)
    }
}
