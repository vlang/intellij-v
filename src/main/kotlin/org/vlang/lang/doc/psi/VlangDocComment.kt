package org.vlang.lang.doc.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocCommentBase
import org.vlang.lang.psi.VlangNamedElement

interface VlangDocComment : PsiDocCommentBase {
    override fun getOwner(): VlangNamedElement?

    /**
     * [getOwnerNameRangeInElement] returns the range of the name of the owner element in the comment
     * or `null` if the comment is invalid.
     */
    fun getOwnerNameRangeInElement(): TextRange?

    /**
     * [isValidDoc] checks if the comment starts with name of the owner element
     *
     * For example, in the following code:
     *
     * ```vlang
     * // foo
     * fn foo() {}
     * ```
     *
     * `foo` is the name of the owner element and the comment is **valid**
     *
     * In the following code:
     *
     * ```vlang
     * // bar
     * fn foo() {}
     * ```
     *
     * `bar` is not the name of the owner element and the comment is **invalid**
     */
    fun isValidDoc(): Boolean

    val codeFences: List<VlangDocCodeFence>

    val linkDefinitions: List<VlangDocLinkDefinition>

    val linkReferenceMap: Map<String, VlangDocLinkDefinition>
}
