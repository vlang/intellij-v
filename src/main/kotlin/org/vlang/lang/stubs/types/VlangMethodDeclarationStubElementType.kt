package org.vlang.lang.stubs.types

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.impl.VlangMethodDeclarationImpl
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangMethodDeclarationStub
import org.vlang.lang.stubs.index.VlangMethodIndex

class VlangMethodDeclarationStubElementType(name: String) :
    VlangNamedStubElementType<VlangMethodDeclarationStub, VlangMethodDeclaration>(name) {

    override fun createPsi(stub: VlangMethodDeclarationStub): VlangMethodDeclaration {
        return VlangMethodDeclarationImpl(stub, this)
    }

    override fun createStub(psi: VlangMethodDeclaration, parentStub: StubElement<*>?): VlangMethodDeclarationStub {
        return VlangMethodDeclarationStub(parentStub, this, psi.name, true, calcTypeText(psi))
    }

    override fun serialize(stub: VlangMethodDeclarationStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getName())
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeName("")
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangMethodDeclarationStub {
        return VlangMethodDeclarationStub(
            parentStub,
            this,
            dataStream.readName(),
            dataStream.readBoolean(),
            dataStream.readName()
        )
    }

    override fun indexStub(stub: VlangMethodDeclarationStub, sink: IndexSink) {
        super.indexStub(stub, sink)
        val typeName = /*stub.getTypeName()*/ ""
        if (!StringUtil.isEmpty(typeName)) {
            val parent: StubElement<*> = stub.parentStub
            if (parent is VlangFileStub) {
                val packageName = parent.getPackageName()
                if (!StringUtil.isEmpty(typeName)) {
                    sink.occurrence(VlangMethodIndex.KEY, "$packageName.$typeName")
                }
            }
        }
    }

    companion object {
        val EMPTY_ARRAY: Array<VlangMethodDeclaration?> = arrayOfNulls(0)
        val ARRAY_FACTORY = ArrayFactory<VlangMethodDeclaration> { count: Int ->
            if (count == 0) EMPTY_ARRAY else arrayOfNulls<VlangMethodDeclaration>(count)
        }

        fun calcTypeText(psi: VlangMethodDeclaration): String? {
//            val reference = VlangPsiImplUtil.getTypeReference(psi.getReceiverType())
//            return if (reference != null) reference.getIdentifier().getText() else null
            return ""
        }
    }
}
