package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.containers.ContainerUtil
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.impl.VlangEnumDeclarationImpl
import org.vlang.lang.stubs.VlangEnumDeclarationStub
import org.vlang.lang.stubs.index.VlangEnumIndex

class VlangEnumDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangEnumDeclarationStub, VlangEnumDeclaration>(name) {

    override fun createPsi(stub: VlangEnumDeclarationStub) = VlangEnumDeclarationImpl(stub, this)

    override fun createStub(psi: VlangEnumDeclaration, parentStub: StubElement<*>?) =
        VlangEnumDeclarationStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())

    override fun serialize(stub: VlangEnumDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangEnumDeclarationStub {
        return VlangEnumDeclarationStub(
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
            ContainerUtil.newArrayList(VlangEnumIndex.KEY)
    }
}
