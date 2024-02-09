package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.*

open class VlangVarProcessor(
    requestedName: PsiElement,
    origin: PsiElement,
    completion: Boolean,
    isCodeFragment: Boolean = false,
) : VlangScopeProcessorBase(requestedName, origin, completion, isCodeFragment) {

    constructor(origin: PsiElement, completion: Boolean) : this(origin, origin, completion)

    override fun crossOff(e: PsiElement): Boolean {
        if (origin is VlangTypeReferenceExpression && e is VlangParamDefinition) {
            return true
        }

        if (e is VlangVarDefinition) {
            val decl = e.parent as VlangVarDeclaration
            // forbid to resolve to `<var>` inside `<var> := <caret>`
            return decl.expressionList.any { PsiTreeUtil.isAncestor(it, origin, false) }
        }

        return e !is VlangParamDefinition &&
                e !is VlangReceiver &&
                e !is VlangEmbeddedDefinition &&
                e !is VlangConstDefinition &&
                e !is VlangGenericParameter &&
                e !is VlangStructDeclaration
    }
}
