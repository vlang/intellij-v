package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import com.intellij.util.containers.ContainerUtil
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangUnionDeclaration
import org.vlang.lang.psi.impl.VlangUnionDeclarationImpl
import org.vlang.lang.stubs.VlangUnionDeclarationStub
import org.vlang.lang.stubs.index.VlangUnionIndex

class VlangUnionDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangUnionDeclarationStub, VlangUnionDeclaration>(name) {

    override fun createPsi(stub: VlangUnionDeclarationStub) = VlangUnionDeclarationImpl(stub, this)

    override fun createStub(psi: VlangUnionDeclaration, parentStub: StubElement<*>?) =
        VlangUnionDeclarationStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())

    override fun serialize(stub: VlangUnionDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangUnionDeclarationStub {
        return VlangUnionDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
            dataStream.readBoolean()
        )
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS: ArrayList<StubIndexKey<String, out VlangNamedElement>> =
            ContainerUtil.newArrayList(VlangUnionIndex.KEY)

        private val EMPTY_ARRAY: Array<VlangUnionDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangUnionDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangUnionDeclaration>(count)
        }
    }
}
