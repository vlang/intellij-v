package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangImportPath
import org.vlang.lang.psi.impl.VlangImportPathImpl
import org.vlang.lang.stubs.VlangImportPathStub

class VlangImportPathStubElementType(name: String) : VlangNamedStubElementType<VlangImportPathStub, VlangImportPath>(name) {
    override fun createPsi(stub: VlangImportPathStub): VlangImportPath {
        return VlangImportPathImpl(stub, this)
    }

    override fun createStub(psi: VlangImportPath, parentStub: StubElement<*>?): VlangImportPathStub {
        return VlangImportPathStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())
    }

    override fun serialize(stub: VlangImportPathStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangImportPathStub {
        return VlangImportPathStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return super.shouldCreateStub(node) && PsiTreeUtil.getParentOfType(node.psi, VlangFunctionOrMethodDeclaration::class.java) == null
    }
}
