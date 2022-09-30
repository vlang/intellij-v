package org.vlang.lang.stubs

import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.stubs.types.VlangModuleClauseStubElementType

class VlangFileStub(file: VlangFile?, private val buildFlags: StringRef) : PsiFileStubImpl<VlangFile?>(file) {
    constructor(file: VlangFile) : this(file, StringRef.fromString(""))

    override fun getType() = VlangFileElementType.INSTANCE

    fun getBuildFlags(): String? = buildFlags.string

    private fun getModuleClauseStub(): StubElement<VlangModuleClause>? {
        return findChildStubByType(VlangModuleClauseStubElementType.INSTANCE)
    }

    fun getModuleName(): String? {
        val stub = getModuleClauseStub()
        return if (stub is VlangModuleClauseStub) stub.name else null
    }

    fun getModuleQualifiedName(): String? {
        val stub = getModuleClauseStub()
        return if (stub is VlangModuleClauseStub) stub.qualifiedName else null
    }
}
