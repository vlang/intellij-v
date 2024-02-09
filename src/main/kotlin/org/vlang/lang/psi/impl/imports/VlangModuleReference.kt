package org.vlang.lang.psi.impl.imports

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangImportAliasName
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.impl.*
import org.vlang.lang.stubs.index.VlangModulesIndex

class VlangModuleReference<T : PsiElement>(element: T) : VlangCachedReference<T>(element) {
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

        return processModules(processor, state)
    }

    private fun processModules(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val element = element

        val fqn = when (element) {
            is VlangImportAliasName -> {
                val importSpec = element.parentOfType<VlangImportSpec>() ?: return true
                importSpec.importPath.qualifiedName
            }

            is VlangImportName      -> element.getQualifiedName()
            else                    -> return true
        }


        val modules = VlangModulesIndex.find(fqn, element.project, null, null)
        if (modules.isEmpty()) {
            return findModuleDirectory(fqn, processor, state)
        }

        return processModules(modules, processor, state, fqn)
    }

    private fun processModules(
        modules: Collection<VlangFile>,
        processor: VlangScopeProcessor,
        state: ResolveState,
        name: String?,
    ) = modules.any {
        val dir = it.parent ?: return@any false
        val module = VlangModule.fromDirectory(dir)
        !processor.execute(module.toPsi(), state.put(VlangReferenceBase.ACTUAL_NAME, name))
    }

    private fun findModuleDirectory(
        name: String,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        // Since the index only stores module files, if a module doesn't
        // contain files, it won't be in the index.
        // Therefore, we need to find the nearest existing file in this
        // module and from there find the folder that contains the original
        // module we're looking for.

        val submodules = VlangModulesIndex.getSubmodulesOfAnyDepth(project, name)
        val minSubmodule = submodules.minByOrNull { it.name } ?: return true

        val countMinSubmoduleDots = minSubmodule.getModuleQualifiedName().count { it == '.' }
        val countDotsInSearchName = name.count { it == '.' }

        var parent = minSubmodule.parent
        for (i in 0 until countMinSubmoduleDots - countDotsInSearchName) {
            parent = parent?.parent
        }

        if (parent != null) {
            val module = VlangModule.fromDirectory(parent)
            return !processor.execute(module.toPsi(), state.put(VlangReferenceBase.ACTUAL_NAME, name))
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
