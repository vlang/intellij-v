package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.impl.VlangEnumDeclarationImpl
import org.vlang.lang.stubs.VlangEnumDeclarationStub
import org.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangEnumDeclarationStubElementType(name: String) : VlangNamedStubElementType<VlangEnumDeclarationStub, VlangEnumDeclaration>(name) {
    override fun createPsi(stub: VlangEnumDeclarationStub): VlangEnumDeclaration {
        return VlangEnumDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangEnumDeclaration, parentStub: StubElement<*>?): VlangEnumDeclarationStub {
        return VlangEnumDeclarationStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangEnumDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangEnumDeclarationStub {
        return VlangEnumDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS = listOf(VlangClassLikeIndex.KEY)

        private val EMPTY_ARRAY: Array<VlangEnumDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangEnumDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangEnumDeclaration>(count)
        }
    }
}
