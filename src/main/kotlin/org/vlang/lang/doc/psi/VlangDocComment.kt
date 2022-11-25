package org.vlang.lang.doc.psi

import com.intellij.psi.PsiDocCommentBase
import org.vlang.lang.psi.VlangNamedElement

interface VlangDocComment : PsiDocCommentBase {
    override fun getOwner(): VlangNamedElement?

    val codeFences: List<VlangDocCodeFence>

    val linkDefinitions: List<VlangDocLinkDefinition>

    val linkReferenceMap: Map<String, VlangDocLinkDefinition>
}
