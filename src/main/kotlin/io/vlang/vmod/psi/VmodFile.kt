package io.vlang.vmod.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import io.vlang.ide.ui.VIcons
import io.vlang.vmod.VmodFileType
import io.vlang.vmod.VmodLanguage

class VmodFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VmodLanguage.INSTANCE) {
    override fun getFileType() = VmodFileType.INSTANCE

    override fun toString() = "V module file"

    override fun getIcon(flags: Int) = VIcons.Module

    override fun getReference() = references.getOrNull(0)

    override fun getReferences(): Array<PsiReference?> = ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
