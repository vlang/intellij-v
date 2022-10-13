package org.vlang.lang.psi.impl.imports

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.search.GlobalSearchScope
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.impl.*
import org.vlang.lang.stubs.index.VlangModulesIndex

class VlangImportReference<T : PsiElement>(element: T, private val importSpec: VlangImportSpec) : VlangCachedReference<T>(element) {

    override fun bindToElement(element: PsiElement): PsiElement {
        return element // TODO
    }

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
        val element = element

        if (element is VlangImportName) {
            val qualifier = element.qualifier
            val identifier = element.identifier

            val name = if (qualifier.isNotEmpty()) {
                qualifier + "." + identifier.text
            } else {
                identifier.text
            }

            val modules = VlangModulesIndex.find(name, element.project, null, null)
            if (modules.isEmpty()) {
                return findModuleDirectory(element, name, processor, state)
            }

            return processModules(modules, processor, state, name)
        }

        val name = importSpec.importPath.text ?: return true

        val modules = VlangModulesIndex.find(name, element.project, GlobalSearchScope.allScope(element.project), null)

        return modules.any {
            val dir = it.parent ?: return@any false
            !processor.execute(dir, state.put(VlangReferenceBase.ACTUAL_NAME, name))
        }
    }

    private fun processModules(
        modules: Collection<VlangFile>,
        processor: VlangScopeProcessor,
        state: ResolveState,
        name: String?,
    ) = modules.any {
        val dir = it.parent ?: return@any false
        !processor.execute(dir, state.put(VlangReferenceBase.ACTUAL_NAME, name))
    }

    private fun findModuleDirectory(
        element: VlangImportName,
        name: String,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        // Since the index only stores module files, if a module doesn't
        // contain files, it won't be in the index.
        // Therefore, we need to find the nearest existing file in this
        // module and from there find the folder that contains the original
        // module we're looking for.

        val submodules = VlangModulesIndex.getSubmodulesOfAnyDepth(element.project, name)
        val minSubmodule = submodules.minByOrNull { it.name } ?: return true

        val countMinSubmoduleDots = minSubmodule.getModuleQualifiedName().count { it == '.' }
        val countDotsInSearchName = name.count { it == '.' }

        var parent = minSubmodule.parent
        for (i in 0 until countMinSubmoduleDots - countDotsInSearchName) {
            parent = parent?.parent
        }

        if (parent != null) {
            return !processor.execute(parent, state.put(VlangReferenceBase.ACTUAL_NAME, name))
        }

        return true
    }

    private fun createContextOnElement(element: PsiElement): ResolveState {
        return ResolveState.initial().put(
            VlangPsiImplUtil.CONTEXT,
            SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
        )
    }
}