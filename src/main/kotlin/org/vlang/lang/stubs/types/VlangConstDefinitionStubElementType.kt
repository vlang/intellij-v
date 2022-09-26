package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangConstDefinition
import org.vlang.lang.psi.impl.VlangConstDefinitionImpl
import org.vlang.lang.stubs.VlangConstDefinitionStub

class VlangConstDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangConstDefinitionStub, VlangConstDefinition>(name) {
    override fun createPsi(stub: VlangConstDefinitionStub): VlangConstDefinition {
        return VlangConstDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangConstDefinition, parentStub: StubElement<*>?): VlangConstDefinitionStub {
        return VlangConstDefinitionStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())
    }

    override fun serialize(stub: VlangConstDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangConstDefinitionStub {
        return VlangConstDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    companion object {
        private val EMPTY_ARRAY: Array<VlangConstDefinition?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangConstDefinition> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangConstDefinition>(count)
        }
    }
}
