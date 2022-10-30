package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.impl.VlangFunctionDeclarationImpl
import org.vlang.lang.stubs.VlangFunctionDeclarationStub

class VlangFunctionDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangFunctionDeclarationStub, VlangFunctionDeclaration>(name) {

    override fun createPsi(stub: VlangFunctionDeclarationStub): VlangFunctionDeclaration {
        return VlangFunctionDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangFunctionDeclaration, parentStub: StubElement<*>?): VlangFunctionDeclarationStub {
        return VlangFunctionDeclarationStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangFunctionDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangFunctionDeclarationStub {
        return VlangFunctionDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }

    companion object {
        val EMPTY_ARRAY: Array<VlangFunctionDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangFunctionDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangFunctionDeclaration>(count)
        }
    }
}