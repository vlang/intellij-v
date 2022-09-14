package org.vlang.lang.psi.impl

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import org.vlang.lang.psi.VlangFunctionOrMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement

abstract class VlangScopeProcessorBase(
    private val requestedNameElement: PsiElement,
    val origin: PsiElement,
    private val myIsCompletion: Boolean,
) : VlangScopeProcessor() {

    private val result = mutableSetOf<VlangNamedElement>()

    constructor(origin: PsiElement) : this(origin, origin, false) {}

    override fun execute(e: PsiElement, state: ResolveState): Boolean {
        if (e is VlangFunctionOrMethodDeclaration) {
            return false
        }

        if (e !is VlangNamedElement) {
            return true
        }

        val name = e.name ?: return false
        if (StringUtil.isEmpty(name) || !myIsCompletion && !requestedNameElement.textMatches(name)) {
            return true
        }
        if (crossOff(e)) {
            return true
        }

        if (e == origin) {
            return true
        }

        return add(e) || myIsCompletion
    }

    protected open fun add(psiElement: VlangNamedElement): Boolean {
        return !result.add(psiElement)
    }

    fun getResult(): VlangNamedElement? = result.firstOrNull()

    fun getVariants() = result

    protected abstract fun crossOff(e: PsiElement): Boolean
}
