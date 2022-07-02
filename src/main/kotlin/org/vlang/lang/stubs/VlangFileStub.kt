package org.vlang.lang.stubs

import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.util.io.StringRef
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangPackageClause
import org.vlang.lang.stubs.types.VlangPackageClauseStubElementType

class VlangFileStub(file: VlangFile?, private val myBuildFlags: StringRef) : PsiFileStubImpl<VlangFile?>(file) {
    constructor(file: VlangFile) : this(file, StringRef.fromString(""))

    override fun getType() = VlangFileElementType.INSTANCE

    fun getBuildFlags(): String? {
        return myBuildFlags.string
    }

    fun getPackageClauseStub(): StubElement<VlangPackageClause>? {
        return findChildStubByType(VlangPackageClauseStubElementType.INSTANCE)
    }

    fun getPackageName(): String? {
        val stub = getPackageClauseStub()
        return if (stub is VlangPackageClauseStub) stub.getName() else null
    }
}