package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*

open class VlangVarProcessor(
    requestedName: PsiElement,
    origin: PsiElement,
    completion: Boolean,
) : VlangScopeProcessorBase(requestedName, origin, completion) {

    constructor(origin: PsiElement, completion: Boolean) : this(origin, origin, completion)

    override fun crossOff(e: PsiElement): Boolean {
        if (origin is VlangTypeReferenceExpression && e is VlangParamDefinition) {
            return true
        }

        return e !is VlangVarDefinition &&
                e !is VlangParamDefinition &&
                e !is VlangReceiver &&
                e !is VlangEmbeddedDefinition &&
                e !is VlangConstDefinition &&
                e !is VlangGenericParameter &&
                e !is VlangStructDeclaration
    }
}
