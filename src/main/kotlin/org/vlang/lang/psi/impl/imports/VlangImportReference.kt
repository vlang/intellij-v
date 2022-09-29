package org.vlang.lang.psi.impl.imports

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.impl.*
import org.vlang.lang.stubs.index.VlangPackagesIndex

class VlangImportReference<T: PsiElement>(element: T, private val importSpec: VlangImportSpec) : VlangCachedReference<T>(element) {
    override fun resolveInner(): PsiElement? {
        val result = mutableListOf<PsiElement>()
        val p = object : VlangScopeProcessorBase(element) {
            override fun execute(e: PsiElement, state: ResolveState): Boolean {
                result.add(e)
                return false
            }

            override fun crossOff(e: PsiElement): Boolean {
                return false
            }
        }
        processResolveVariants(p)
        return result.firstOrNull()
    }

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        val state = createContextOnElement(element)

        if (!processModules(processor, state)) {
            return false
        }
        return true
    }

    private fun processModules(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val name = importSpec.importPath.text ?: return true

        val modules = VlangPackagesIndex.find(name, element.project, GlobalSearchScope.allScope(element.project), null)

        return modules.any {
            val dir = it.parent ?: return@any false
            !processor.execute(dir, state.put(VlangReferenceBase.ACTUAL_NAME, name))
        }
    }

    private fun createContextOnElement(element: PsiElement): ResolveState {
        return ResolveState.initial().put(
            VlangPsiImplUtil.CONTEXT,
            SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
        )
    }
}
