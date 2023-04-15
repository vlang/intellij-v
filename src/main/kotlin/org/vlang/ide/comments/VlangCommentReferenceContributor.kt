package org.vlang.ide.comments

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.VlangFile

class VlangCommentReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            psiElement(PsiComment::class.java)
                .inFile(
                    psiElement(VlangFile::class.java)
                ),
            VlangCommentReferenceProvider()
        )
    }

    class VlangCommentReferenceProvider : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
            val comment = element as? VlangDocComment ?: return emptyArray()
            val range = comment.getOwnerNameRangeInElement() ?: return emptyArray()
            return arrayOf(
                VlangCommentLinkReference(comment, range)
            )
        }
    }
}
