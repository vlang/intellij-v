package org.vlang.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import org.vlang.lang.VlangFileType
import org.vlang.lang.VlangLanguage

class VlangFile(viewProvider: FileViewProvider) :
    PsiFileBase(viewProvider, VlangLanguage.INSTANCE), PsiImportHolder, PsiClassOwner {

    override fun getFileType() = VlangFileType.INSTANCE

    override fun toString() = "Vlang Language file"

    override fun getIcon(flags: Int) = IconLoader.getIcon("/icons/vlang.svg", this::class.java)

    override fun getReference() = references.getOrNull(0)

    override fun getReferences(): Array<PsiReference?> = ReferenceProvidersRegistry.getReferencesFromProviders(this)

    override fun getClasses(): Array<PsiClass> = emptyArray()

    override fun getPackageName() = ""

    override fun setPackageName(packageName: String?) {}

    override fun importClass(aClass: PsiClass) = false
}
