package io.vlang.lang.stubs

import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import io.vlang.lang.VlangFileElementType
import io.vlang.lang.psi.VlangFile
import io.vlang.lang.psi.VlangModuleClause

class VlangFileStub(file: VlangFile?) : PsiFileStubImpl<VlangFile?>(file) {
    override fun getType() = VlangFileElementType.INSTANCE

    private fun getModuleClauseStub(): StubElement<VlangModuleClause>? {
        return findChildStubByType(VlangElementTypeFactory.MODULE_CLAUSE)
    }

    fun getModuleName(): String? {
        val stub = getModuleClauseStub()
        return if (stub is VlangModuleClauseStub) stub.name else null
    }

    fun getModuleQualifiedName(): String? {
        val stub = getModuleClauseStub()
        return if (stub is VlangModuleClauseStub) stub.qualifiedName else psi?.getModuleQualifiedName()
    }
}
