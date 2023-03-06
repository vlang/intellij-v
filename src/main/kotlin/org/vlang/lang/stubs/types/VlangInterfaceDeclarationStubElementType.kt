package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.impl.VlangInterfaceDeclarationImpl
import org.vlang.lang.stubs.VlangInterfaceDeclarationStub
import org.vlang.lang.stubs.VlangTypeStub
import org.vlang.lang.stubs.index.VlangClassLikeIndex
import org.vlang.lang.stubs.index.VlangInterfaceInheritorsIndex

class VlangInterfaceDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangInterfaceDeclarationStub, VlangInterfaceDeclaration>(name) {

    override fun createPsi(stub: VlangInterfaceDeclarationStub): VlangInterfaceDeclaration {
        return VlangInterfaceDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangInterfaceDeclaration, parentStub: StubElement<*>?): VlangInterfaceDeclarationStub {
        return VlangInterfaceDeclarationStub(parentStub, this, psi.name, psi.isPublic())
    }

    override fun serialize(stub: VlangInterfaceDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangInterfaceDeclarationStub {
        return VlangInterfaceDeclarationStub(parentStub, this, dataStream.readName(), dataStream.readBoolean())
    }

    override fun indexStub(stub: VlangInterfaceDeclarationStub, sink: IndexSink) {
        super.indexStub(stub, sink)

        val typeStub =  stub.childrenStubs.firstOrNull() ?: return
        typeStub.childrenStubs.forEach {
            if (it is VlangTypeStub) {
                val name = it.getText() ?: return@forEach
                sink.occurrence(VlangInterfaceInheritorsIndex.KEY, name)
            }
        }
    }

    override fun getExtraIndexKeys() = EXTRA_KEYS

    companion object {
        private val EXTRA_KEYS = listOf(VlangClassLikeIndex.KEY)

        private val EMPTY_ARRAY: Array<VlangInterfaceDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangInterfaceDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangInterfaceDeclaration>(count)
        }
    }
}
