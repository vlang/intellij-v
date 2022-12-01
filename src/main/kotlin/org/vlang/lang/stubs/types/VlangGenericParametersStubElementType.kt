package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangGenericParameters
import org.vlang.lang.psi.impl.VlangGenericParametersImpl
import org.vlang.lang.stubs.VlangGenericParametersStub

class VlangGenericParametersStubElementType(name: String) : VlangStubElementType<VlangGenericParametersStub, VlangGenericParameters>(name) {
    override fun createPsi(stub: VlangGenericParametersStub): VlangGenericParameters {
        return VlangGenericParametersImpl(stub, this)
    }

    override fun createStub(psi: VlangGenericParameters, parentStub: StubElement<*>?): VlangGenericParametersStub {
        val parameters = psi.parameters.map { it.text }
        return VlangGenericParametersStub(parentStub, this, parameters)
    }

    override fun serialize(stub: VlangGenericParametersStub, dataStream: StubOutputStream) {
        dataStream.writeInt(stub.parameters.size)
        stub.parameters.forEach { dataStream.writeName(it) }
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangGenericParametersStub {
        val size = dataStream.readInt()
        val parameters = (0 until size).map { dataStream.readName()?.string ?: "" }
        return VlangGenericParametersStub(parentStub, this, parameters)
    }
}
