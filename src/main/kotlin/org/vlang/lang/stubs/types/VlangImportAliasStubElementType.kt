package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangImportAlias
import org.vlang.lang.psi.impl.VlangImportAliasImpl
import org.vlang.lang.stubs.VlangImportAliasStub

class VlangImportAliasStubElementType(name: String) : VlangNamedStubElementType<VlangImportAliasStub, VlangImportAlias>(name) {
    override fun createPsi(stub: VlangImportAliasStub): VlangImportAlias {
        return VlangImportAliasImpl(stub, this)
    }

    override fun createStub(psi: VlangImportAlias, parentStub: StubElement<*>?): VlangImportAliasStub {
        return VlangImportAliasStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())
    }

    override fun serialize(stub: VlangImportAliasStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangImportAliasStub {
        return VlangImportAliasStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return super.shouldCreateStub(node) && PsiTreeUtil.getParentOfType(node.psi, VlangFunctionOrMethodDeclaration::class.java) == null
    }
}
