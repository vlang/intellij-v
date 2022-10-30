package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.Processor
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil

class VlangMethodInheritorsSearch : QueryExecutorBase<VlangSignatureOwner, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameter: DefinitionsScopedSearch.SearchParameters,
        processor: Processor<in VlangSignatureOwner>,
    ) {
        if (!parameter.isCheckDeep) return

        val method = parameter.element as? VlangInterfaceMethodDefinition ?: return
        val interfaceType = PsiTreeUtil.getStubOrPsiParentOfType(method, VlangInterfaceType::class.java)
        val decl = interfaceType?.parentOfType<VlangInterfaceDeclaration>() ?: return
        if (!interfaceType.isValid) return

        VlangInheritorsSearch().processMethodOwners({ owner: VlangNamedElement ->
            val struct = owner as VlangStructDeclaration
            val name = method.name ?: return@processMethodOwners true
            val structMethod = VlangLangUtil.findMethod(struct.structType, name)

            structMethod == null || structMethod == method || processor.process(structMethod)
        }, decl, interfaceType, mutableListOf(method), listOf())
    }
}
