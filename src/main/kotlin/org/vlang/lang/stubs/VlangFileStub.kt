package org.vlang.lang.stubs

import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.stubs.types.VlangModuleClauseStubElementType

class VlangFileStub(file: VlangFile?, private val myBuildFlags: StringRef) : PsiFileStubImpl<VlangFile?>(file) {
    constructor(file: VlangFile) : this(file, StringRef.fromString(""))

    override fun getType() = VlangFileElementType.INSTANCE

    fun getBuildFlags(): String? {
        return myBuildFlags.string
    }

    private fun getModuleClauseStub(): StubElement<VlangModuleClause>? {
        return findChildStubByType(VlangModuleClauseStubElementType.INSTANCE)
    }

    fun getModuleName(): String? {
        val stub = getModuleClauseStub()
        return if (stub is VlangModuleClauseStub) stub.getName() else null
    }
}
