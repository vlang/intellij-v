package org.vlang.lang.stubs.types

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import org.vlang.lang.psi.VlangPackageClause
import org.vlang.lang.psi.impl.VlangPackageClauseImpl
import org.vlang.lang.stubs.VlangPackageClauseStub

class VlangPackageClauseStubElementType :
    VlangStubElementType<VlangPackageClauseStub, VlangPackageClause>("PACKAGE_CLAUSE") {

    override fun createPsi(stub: VlangPackageClauseStub): VlangPackageClause {
        return VlangPackageClauseImpl(stub, this)
    }

    override fun createStub(psi: VlangPackageClause, parentStub: StubElement<*>): VlangPackageClauseStub {
        return VlangPackageClauseStub(parentStub, this, psi.identifier?.text ?: "") // TODO
    }

    override fun serialize(stub: VlangPackageClauseStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.myName)
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>): VlangPackageClauseStub {
        return VlangPackageClauseStub(parentStub, this, dataStream.readName())
    }

    companion object {
        val INSTANCE = VlangPackageClauseStubElementType()
    }
}
