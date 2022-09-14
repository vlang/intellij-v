package org.vlang.lang.psi.impl

import com.intellij.openapi.util.Comparing
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

//    override fun add(o: VlangNamedElement): Boolean {
//        val commonParent = PsiTreeUtil.findCommonParent(o, myOrigin)
//        if (commonParent is VlangRangeClause || commonParent is VlangTypeSwitchGuard) return true
//        val p: PsiElement = o.getParent()
//        val inVarOrRange = PsiTreeUtil.getParentOfType(o, VlangVarDeclaration::class.java) != null || p is VlangRangeClause
//        val differentBlocks = differentBlocks(o)
//        val inShortVar = PsiTreeUtil.getParentOfType(o, VlangShortVarDeclaration::class.java, VlangRecvStatement::class.java) != null
//        if (differentBlocks) return true
//        if (differentBlocks && inShortVar && !inVarOrRange && result != null && !myIsCompletion) return true
//        if (inShortVar && fromNotAncestorBlock(o)) return true
//        return super.add(o)
//    }

//    private fun differentBlocks(o: VlangNamedElement?): Boolean {
//        return !Comparing.equal<Any>(scope, getScope(o))
//    }

    override fun crossOff(e: PsiElement): Boolean {
        return e !is VlangVarDefinition &&
                e !is VlangParamDefinition &&
                e !is VlangReceiver &&
                e !is VlangAnonymousFieldDefinition &&
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

            val elseStatement = element.parentOfType<VlangElseStatement>()
            if (elseStatement != null) {
                return elseStatement.block
            }

            return PsiTreeUtil.getParentOfType(element, VlangBlock::class.java)
        }
    }
}
