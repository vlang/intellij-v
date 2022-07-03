package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.impl.VlangModuleClauseImpl
import org.vlang.lang.stubs.VlangModuleClauseStub

class VlangModuleClauseStubElementType :
    VlangStubElementType<VlangModuleClauseStub, VlangModuleClause>("MODULE_CLAUSE") {

    override fun createPsi(stub: VlangModuleClauseStub): VlangModuleClause {
        return VlangModuleClauseImpl(stub, this)
    }

    override fun createStub(psi: VlangModuleClause, parentStub: StubElement<*>): VlangModuleClauseStub {
        return VlangModuleClauseStub(parentStub, this, psi.identifier?.text ?: "") // TODO
    }

    override fun serialize(stub: VlangModuleClauseStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.myName)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>): VlangModuleClauseStub {
        return VlangModuleClauseStub(parentStub, this, dataStream.readName())
    }

    companion object {
        val INSTANCE = VlangModuleClauseStubElementType()
    }
}
