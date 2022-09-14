package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangType
import org.vlang.lang.stubs.VlangTypeStub
import java.io.IOException

abstract class VlangTypeStubElementType(name: String) : VlangStubElementType<VlangTypeStub, VlangType>(name) {
    override fun createStub(psi: VlangType, parentStub: StubElement<*>?): VlangTypeStub {
        return VlangTypeStub(parentStub, this, psi.text)
    }

    @Throws(IOException::class)
    override fun serialize(stub: VlangTypeStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.getText())
    }

    override fun shouldCreateStubInBlock(node: ASTNode): Boolean {
        return /*PsiTreeUtil.getParentOfType(node.psi, VlangTypeSpec::class.java) != null || */super.shouldCreateStubInBlock(node)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangTypeStub {
        return VlangTypeStub(parentStub, this, dataStream.readName())
    }
}
