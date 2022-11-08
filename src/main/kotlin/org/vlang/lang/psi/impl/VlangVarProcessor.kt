package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*

open class VlangVarProcessor(
    requestedName: PsiElement,
    origin: PsiElement,
    completion: Boolean,
    delegate: Boolean,
) : VlangScopeProcessorBase(requestedName, origin, completion) {

    private val scope = getScope(origin)

    constructor(origin: PsiElement, completion: Boolean) : this(origin, origin, completion, false)

    override fun add(o: VlangNamedElement): Boolean {
        // TODO: handle different scopes
//        val commonParent = PsiTreeUtil.findCommonParent(o, myOrigin)
//        if (commonParent is VlangRangeClause || commonParent is VlangTypeSwitchGuard) return true
//        val p: PsiElement = o.getParent()
//        val inVarOrRange = PsiTreeUtil.getParentOfType(o, VlangVarDeclaration::class.java) != null || p is VlangRangeClause
//        val differentBlocks = differentBlocks(o)
//        val inShortVar = PsiTreeUtil.getParentOfType(o, VlangShortVarDeclaration::class.java, VlangRecvStatement::class.java) != null
//        if (differentBlocks && fromNotAncestorBlock(o)) return true
//        if (differentBlocks && inShortVar && !inVarOrRange && result != null && !myIsCompletion) return true
//        if (inShortVar && fromNotAncestorBlock(o)) return true
        return super.add(o)
    }

    open fun fromNotAncestorBlock(o: VlangNamedElement): Boolean {
        return !PsiTreeUtil.isAncestor(getScope(o), origin, false)
    }

    private fun differentBlocks(o: VlangNamedElement?): Boolean {
        return scope != getScope(o)
    }

    override fun crossOff(e: PsiElement): Boolean {
        return e !is VlangVarDefinition &&
                e !is VlangParamDefinition &&
                e !is VlangReceiver &&
                e !is VlangEmbeddedDefinition &&
                e !is VlangConstDefinition
    }

    companion object {
        fun getScope(element: PsiElement?): VlangCompositeElement? {
            if (element == null) return null

            val forStatement = element.parentOfType<VlangForStatement>()
            if (forStatement != null) {
                return forStatement.block
            }

            val ifStatement = element.parentOfType<VlangIfStatement>()
            if (ifStatement != null) {
                return ifStatement.block
            }

            val ifExpression = element.parentOfType<VlangIfExpression>()
            if (ifExpression != null) {
                return ifExpression.block
            }

            val elseStatement = element.parentOfType<VlangElseStatement>()
            if (elseStatement != null) {
                return elseStatement.block
            }

            return PsiTreeUtil.getParentOfType(element, VlangBlock::class.java)
        }
    }
}
