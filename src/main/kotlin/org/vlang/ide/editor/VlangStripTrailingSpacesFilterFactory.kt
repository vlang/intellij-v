package org.vlang.ide.editor

import com.intellij.lang.Language
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.impl.PsiBasedStripTrailingSpacesFilter
import com.intellij.psi.PsiFile
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.VlangRecursiveVisitor
import org.vlang.lang.psi.VlangStringLiteral

class VlangStripTrailingSpacesFilterFactory : PsiBasedStripTrailingSpacesFilter.Factory() {
    override fun createFilter(document: Document): PsiBasedStripTrailingSpacesFilter = VlangStripTrailingSpacesFilter(document)

    override fun isApplicableTo(language: Language): Boolean = language.isKindOf(VlangLanguage)

    private class VlangStripTrailingSpacesFilter(document: Document) : PsiBasedStripTrailingSpacesFilter(document) {
        override fun process(psiFile: PsiFile) {
            psiFile.accept(object : VlangRecursiveVisitor() {
                override fun visitStringLiteral(o: VlangStringLiteral) {
                    disableRange(o.textRange, false)
                    super.visitStringLiteral(o)
                }
            })
        }
    }
}
