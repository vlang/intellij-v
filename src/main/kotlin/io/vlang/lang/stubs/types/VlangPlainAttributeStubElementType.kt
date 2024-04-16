package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import io.vlang.lang.psi.VlangPlainAttribute
import io.vlang.lang.psi.impl.VlangPlainAttributeImpl
import io.vlang.lang.stubs.VlangPlainAttributeStub

class VlangPlainAttributeStubElementType(name: String) : VlangStubElementType<VlangPlainAttributeStub, VlangPlainAttribute>(name) {
    override fun createPsi(stub: VlangPlainAttributeStub): VlangPlainAttribute {
        return VlangPlainAttributeImpl(stub, this)
    }

    override fun createStub(psi: VlangPlainAttribute, parentStub: StubElement<*>?): VlangPlainAttributeStub {
        return VlangPlainAttributeStub(parentStub, this, psi.text)
    }

    override fun serialize(stub: VlangPlainAttributeStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangPlainAttributeStub {
        return VlangPlainAttributeStub(parentStub, this, dataStream.readName())
    }
}
