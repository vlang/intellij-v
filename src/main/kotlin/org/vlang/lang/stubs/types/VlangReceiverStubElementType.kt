package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangReceiver
import org.vlang.lang.psi.impl.VlangReceiverImpl
import org.vlang.lang.stubs.VlangReceiverStub

class VlangReceiverStubElementType(name: String) : VlangNamedStubElementType<VlangReceiverStub, VlangReceiver>(name) {
    override fun createPsi(stub: VlangReceiverStub): VlangReceiver {
        return VlangReceiverImpl(stub, this)
    }

    override fun createStub(psi: VlangReceiver, parentStub: StubElement<*>?): VlangReceiverStub {
        return VlangReceiverStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())
    }

    override fun serialize(stub: VlangReceiverStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangReceiverStub {
        return VlangReceiverStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return super.shouldCreateStub(node) && PsiTreeUtil.getParentOfType(node.psi, VlangFunctionOrMethodDeclaration::class.java) == null
    }
}
