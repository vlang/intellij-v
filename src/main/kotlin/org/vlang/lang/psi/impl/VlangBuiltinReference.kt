package org.vlang.lang.psi.impl

import com.intellij.openapi.util.Conditions
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveState
import com.intellij.psi.SmartPointerManager
import org.vlang.configurations.VlangConfiguration
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangNamedElement

class VlangBuiltinReference<T : PsiElement>(element: T) : VlangCachedReference<T>(element, element.firstChild.textRangeInParent) {
    override fun bindToElement(element: PsiElement): PsiElement = element

    override fun resolveInner(): PsiElement? {
        val result = mutableListOf<PsiElement>()
        val p = object : VlangScopeProcessorBase(element) {
            override fun execute(e: PsiElement, state: ResolveState): Boolean {
                if (e !is VlangNamedElement) {
                    return true
                }

                val rawName = element.firstChild.text
                val name = "$" + if (rawName == "__offsetof") "offsetof" else rawName
                if (e.name == name) {
                    result.add(e)
                    return false
                }

                return true
            }

            override fun crossOff(e: PsiElement): Boolean = false
        }
        processResolveVariants(p)
        return result.firstOrNull()
    }

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        val state = createContextOnElement(element)
        return processStubs(processor, state)
    }

    private fun processStubs(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val stdlib = VlangConfiguration.getInstance(myElement.project).stubsLocation ?: return true
        val psiManager = PsiManager.getInstance(myElement.project)
        return processStubFile("builtin_compile_time.v", stdlib, psiManager, processor, state)
    }

    private fun processStubFile(
        name: String,
        stdlib: VirtualFile,
        psiManager: PsiManager,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val compileTimeFile = stdlib.findChild(name) ?: return true
        val compileTimePsiFile = psiManager.findFile(compileTimeFile) as? VlangFile ?: return true

        return VlangPsiImplUtil.processNamedElements(
            processor,
            state,
            compileTimePsiFile.getFunctions(),
            Conditions.alwaysTrue(),
            localResolve = false,
            checkContainingFile = false
        )
    }

    private fun createContextOnElement(element: PsiElement): ResolveState {
        return ResolveState.initial().put(
            VlangPsiImplUtil.CONTEXT,
            SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
        )
    }
}
