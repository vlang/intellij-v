package io.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import io.vlang.lang.psi.VlangStructDeclaration
import io.vlang.lang.psi.impl.VlangStructDeclarationImpl
import io.vlang.lang.stubs.VlangStructDeclarationStub
import io.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangStructDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangStructDeclarationStub, VlangStructDeclaration>(name) {

    override fun createPsi(stub: VlangStructDeclarationStub): VlangStructDeclaration {
        return VlangStructDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangStructDeclaration, parentStub: StubElement<*>?): VlangStructDeclarationStub {
        return VlangStructDeclarationStub(parentStub, this, psi.name, psi.isUnion, psi.isPublic())
    }

    override fun serialize(stub: VlangStructDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isUnion)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangStructDeclarationStub {
        return VlangStructDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS = listOf(VlangClassLikeIndex.KEY)

        private val EMPTY_ARRAY: Array<VlangStructDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangStructDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangStructDeclaration>(count)
        }
    }
}
