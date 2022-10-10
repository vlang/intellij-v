package org.vlang.lang.usages

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*

class VlangReadWriteAccessDetector : ReadWriteAccessDetector() {
    override fun isReadWriteAccessible(element: PsiElement): Boolean {
        return element is VlangVarDefinition ||
                element is VlangConstDefinition ||
                element is VlangParamDefinition ||
                element is VlangReceiver ||
                element is VlangFieldDefinition
    }

    override fun isDeclarationWriteAccess(element: PsiElement): Boolean {
        return element is VlangVarDefinition || element is VlangConstDefinition
    }

    override fun getReferenceAccess(referencedElement: PsiElement, reference: PsiReference): Access {
        return getExpressionAccess(reference.element)
    }

    override fun getExpressionAccess(expression: PsiElement): Access {
        if (expression is VlangFieldName) {
            return if (expression.getParent() is VlangKey) Access.Write else Access.Read
        }
        val referenceExpression = expression as? VlangReferenceExpression ?: expression.parentOfType()
        return referenceExpression?.readWriteAccess ?: Access.Read
    }
}
