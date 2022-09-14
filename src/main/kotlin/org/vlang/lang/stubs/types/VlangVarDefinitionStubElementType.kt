package org.vlang.lang.stubs.types

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ArrayFactory
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangVarDefinition
import org.vlang.lang.psi.impl.VlangVarDefinitionImpl
import org.vlang.lang.stubs.VlangVarDefinitionStub

class VlangVarDefinitionStubElementType(name: String) : VlangNamedStubElementType<VlangVarDefinitionStub, VlangVarDefinition>(name) {
    override fun createPsi(stub: VlangVarDefinitionStub): VlangVarDefinition {
        return VlangVarDefinitionImpl(stub, this)
    }

    override fun createStub(psi: VlangVarDefinition, parentStub: StubElement<*>?): VlangVarDefinitionStub {
        return VlangVarDefinitionStub(parentStub, this, psi.name, psi.isPublic(), psi.isGlobal())
    }

    override fun serialize(stub: VlangVarDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeBoolean(stub.isPublic)
        dataStream.writeBoolean(stub.isGlobal)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): VlangVarDefinitionStub {
        return VlangVarDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean(), dataStream.readBoolean())
    }

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return super.shouldCreateStub(node) && PsiTreeUtil.getParentOfType(node.psi, VlangFunctionOrMethodDeclaration::class.java) == null
    }
}
