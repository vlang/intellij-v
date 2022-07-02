package org.vlang.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.util.ArrayUtil
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangReferenceExpressionBase

class VlangReference(o: VlangReferenceExpressionBase) :
    VlangReferenceBase<VlangReferenceExpressionBase?>(o,
    TextRange.from(o.getIdentifier().startOffsetInParent, o.getIdentifier().textLength)
) {
    override fun isReferenceTo(element: PsiElement): Boolean {
        return true
    }

    private val identifier: PsiElement?
        get() = myElement?.getIdentifier()

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        return myElement?.containingFile?.children?.filter {
            it is VlangFunctionDeclaration && it.nameIdentifier?.text == identifier?.text
        }
            ?.map { PsiElementResolveResult(it) }
            ?.toTypedArray() ?: emptyArray()
    }

    override fun getVariants() = ArrayUtil.EMPTY_OBJECT_ARRAY

    override fun handleElementRename(newElementName: String): PsiElement? {
        return identifier
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangReference

        if (element != other.element) return false

        return true
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }

}