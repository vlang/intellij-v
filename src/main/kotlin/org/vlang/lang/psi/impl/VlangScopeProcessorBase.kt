package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangScopeProcessorBase(
    private val requestedNameElement: PsiElement,
    val origin: PsiElement,
    private val isForCompletion: Boolean,
    private val isForCodeFragment: Boolean = false,
) : VlangScopeProcessor() {

    private val result = mutableSetOf<VlangNamedElement>()

    constructor(origin: PsiElement) : this(origin, origin, false)

    override fun execute(e: PsiElement, state: ResolveState): Boolean {
        if (e is VlangFunctionOrMethodDeclaration) {
            return false
        }

        if (e !is VlangNamedElement) {
            return true
        }

        val name = state.get(VlangReferenceBase.SEARCH_NAME) ?: e.name ?: return true
        if (name.isEmpty() || !isForCompletion && !requestedNameElement.textMatches(name)) {
            return true
        }
        if (crossOff(e)) {
            return true
        }

        if (e == origin) {
            return true
        }

        return add(e) || isForCompletion
    }

    protected open fun add(psiElement: VlangNamedElement): Boolean {
        return !result.add(psiElement)
    }

    fun getResult(): VlangNamedElement? = result.firstOrNull()

    fun getVariants() = result

    protected abstract fun crossOff(e: PsiElement): Boolean

    override fun isCompletion() = isForCompletion
    override fun isCodeFragment() = isForCodeFragment
}
