package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangGenericParameter
import org.vlang.lang.psi.impl.VlangGenericParameterImpl
import org.vlang.lang.stubs.VlangGenericParameterStub

class VlangGenericParameterStubElementType(name: String) : VlangNamedStubElementType<VlangGenericParameterStub, VlangGenericParameter>(name) {
    override fun createPsi(stub: VlangGenericParameterStub): VlangGenericParameter {
        return VlangGenericParameterImpl(stub, this)
    }

    override fun createStub(psi: VlangGenericParameter, parentStub: StubElement<*>?): VlangGenericParameterStub {
        return VlangGenericParameterStub(parentStub, this, psi.name)
    }

    override fun serialize(stub: VlangGenericParameterStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangGenericParameterStub {
        return VlangGenericParameterStub(parentStub, this, dataStream.readName())
    }
}
