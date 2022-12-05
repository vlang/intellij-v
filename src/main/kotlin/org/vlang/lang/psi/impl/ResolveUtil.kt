package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import org.vlang.lang.psi.VlangCompositeElement

object ResolveUtil {
    fun treeWalkUp(place: PsiElement?, processor: PsiScopeProcessor, until: (PsiElement) -> Boolean = { false }): Boolean {
        var lastParent: PsiElement? = null
        var run = place
        while (run != null) {
            if (until(run)) {
                return true
            }

            if (place !== run && !run.processDeclarations(processor, ResolveState.initial(), lastParent, place!!)) return false
            lastParent = run
            run = run.parent
        }
        return true
    }

    fun processChildren(
        element: PsiElement,
        processor: PsiScopeProcessor,
        substitutor: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        var run = if (lastParent == null) element.lastChild else lastParent.prevSibling

        while (run != null) {
            if (run is VlangCompositeElement && !run.processDeclarations(processor, substitutor, null, place)) return false
            run = run.prevSibling
        }

        return true
    }

    fun processChildrenFromTop(
        element: PsiElement,
        processor: PsiScopeProcessor,
        substitutor: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        var run = element.firstChild
        while (run != null) {
            if (run is VlangCompositeElement) {
                if (run.isEquivalentTo(lastParent)) return true
                if (!run.processDeclarations(processor, substitutor, null, place)) return false
            }
            run = run.nextSibling
        }
        return true
    }
}
