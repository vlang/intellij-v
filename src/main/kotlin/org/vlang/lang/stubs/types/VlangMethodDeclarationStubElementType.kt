package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.impl.VlangMethodDeclarationImpl
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangMethodDeclarationStub
import org.vlang.lang.stubs.index.VlangMethodIndex

class VlangMethodDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangMethodDeclarationStub, VlangMethodDeclaration>(name) {

    override fun createPsi(stub: VlangMethodDeclarationStub): VlangMethodDeclaration {
        return VlangMethodDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangMethodDeclaration, parentStub: StubElement<*>?): VlangMethodDeclarationStub {
        return VlangMethodDeclarationStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal(), calcTypeText(psi))
    }

    override fun serialize(stub: VlangMethodDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
        dataStream.writeName(stub.typeName)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangMethodDeclarationStub {
        return VlangMethodDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
            dataStream.readBoolean(),
            dataStream.readName()
        )
    }

    override fun indexStub(stub: VlangMethodDeclarationStub, sink: IndexSink) {
        super.indexStub(stub, sink)
        val typeName = stub.typeName ?: return
        if (typeName.isNotEmpty()) {
            val parent: StubElement<*> = stub.parentStub
            if (parent is VlangFileStub) {
                val packageName = parent.getModuleName()
                sink.occurrence(VlangMethodIndex.KEY, "$packageName.$typeName")
            }
        }
    }

    companion object {
        val EMPTY_ARRAY: Array<VlangMethodDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangMethodDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangMethodDeclaration>(count)
        }

        fun calcTypeText(psi: VlangMethodDeclaration): String? {
            val reference = VlangPsiImplUtil.getTypeReference(psi.receiverType)
            return reference?.getIdentifier()?.text
        }
    }
}
