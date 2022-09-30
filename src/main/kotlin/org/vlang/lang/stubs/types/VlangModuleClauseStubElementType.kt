package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.impl.VlangModuleClauseImpl
import org.vlang.lang.stubs.VlangModuleClauseStub

class VlangModuleClauseStubElementType : VlangStubElementType<VlangModuleClauseStub, VlangModuleClause>("MODULE_CLAUSE") {
    override fun createPsi(stub: VlangModuleClauseStub): VlangModuleClause {
        return VlangModuleClauseImpl(stub, this)
    }

    override fun createStub(psi: VlangModuleClause, parentStub: StubElement<*>): VlangModuleClauseStub {
        val file = psi.containingFile as VlangFile
        return VlangModuleClauseStub(parentStub, this, psi.name, file.getModuleQualifiedName())
    }

    override fun serialize(stub: VlangModuleClauseStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeName(stub.qualifiedName)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>): VlangModuleClauseStub {
        return VlangModuleClauseStub(parentStub, this, dataStream.readName(), dataStream.readName())
    }

    companion object {
        val INSTANCE = VlangModuleClauseStubElementType()
    }
}
