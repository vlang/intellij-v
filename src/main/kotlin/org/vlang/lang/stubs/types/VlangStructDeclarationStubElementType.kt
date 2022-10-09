package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import com.intellij.util.containers.ContainerUtil
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.impl.VlangStructDeclarationImpl
import org.vlang.lang.stubs.VlangStructDeclarationStub
import org.vlang.lang.stubs.index.VlangStructIndex

class VlangStructDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangStructDeclarationStub, VlangStructDeclaration>(name) {

    override fun createPsi(stub: VlangStructDeclarationStub): VlangStructDeclaration {
        return VlangStructDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangStructDeclaration, parentStub: StubElement<*>?): VlangStructDeclarationStub {
        return VlangStructDeclarationStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangStructDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangStructDeclarationStub {
        return VlangStructDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS: ArrayList<StubIndexKey<String, out VlangNamedElement>> =
            ContainerUtil.newArrayList(VlangStructIndex.KEY)

        private val EMPTY_ARRAY: Array<VlangStructDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangStructDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangStructDeclaration>(count)
        }
    }
}
