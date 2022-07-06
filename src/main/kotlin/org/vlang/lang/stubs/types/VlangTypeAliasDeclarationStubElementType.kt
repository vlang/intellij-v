package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.containers.ContainerUtil
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangTypeAliasDeclaration
import org.vlang.lang.psi.impl.VlangTypeAliasDeclarationImpl
import org.vlang.lang.stubs.VlangStructDeclarationStub
import org.vlang.lang.stubs.VlangTypeAliasDeclarationStub
import org.vlang.lang.stubs.index.VlangStructIndex
import org.vlang.lang.stubs.index.VlangTypeAliasIndex

class VlangTypeAliasDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangTypeAliasDeclarationStub, VlangTypeAliasDeclaration>(name) {

    override fun createPsi(stub: VlangTypeAliasDeclarationStub) = VlangTypeAliasDeclarationImpl(stub, this)

    override fun createStub(psi: VlangTypeAliasDeclaration, parentStub: StubElement<*>) =
        VlangTypeAliasDeclarationStub(parentStub, this, psi.name, true)

    override fun serialize(stub: VlangTypeAliasDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>) =
        VlangTypeAliasDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS: ArrayList<StubIndexKey<String, out VlangNamedElement>> =
            ContainerUtil.newArrayList(VlangTypeAliasIndex.KEY)
    }
}
