package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.SourceTreeToPsiMap
import org.vlang.lang.VlangTypes

open class VlangWrapperPsiElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun findElementAt(offset: Int): PsiElement? {
        val treeElement = node.findLeafElementAt(offset) ?: return null
        if (treeElement.elementType == VlangTypes.IDENTIFIER) {
            return treeElement.treeParent.psi
        }
        return SourceTreeToPsiMap.treeElementToPsi(treeElement)
    }
}
