package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangGlobalVariableDefinition
import org.vlang.lang.psi.impl.VlangGlobalVariableDefinitionImpl
import org.vlang.lang.stubs.VlangGlobalVariableDefinitionStub

class VlangGlobalVariableDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangGlobalVariableDefinitionStub, VlangGlobalVariableDefinition>(name) {
    override fun createPsi(stub: VlangGlobalVariableDefinitionStub): VlangGlobalVariableDefinition {
        return VlangGlobalVariableDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangGlobalVariableDefinition, parentStub: StubElement<*>?): VlangGlobalVariableDefinitionStub {
        return VlangGlobalVariableDefinitionStub(parentStub, this, psi.name, psi.isPublic(), true)
    }

    override fun serialize(stub: VlangGlobalVariableDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangGlobalVariableDefinitionStub {
        return VlangGlobalVariableDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    companion object {
        private val EMPTY_ARRAY: Array<VlangGlobalVariableDefinition?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangGlobalVariableDefinition> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangGlobalVariableDefinition>(count)
        }
    }
}
