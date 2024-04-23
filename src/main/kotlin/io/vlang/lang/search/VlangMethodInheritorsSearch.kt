package io.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import io.vlang.lang.psi.VlangInterfaceMethodDefinition
import io.vlang.lang.psi.VlangNamedElement
import io.vlang.lang.psi.VlangStructDeclaration
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

object VlangMethodInheritorsSearch : QueryExecutorBase<VlangNamedElement, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameter: DefinitionsScopedSearch.SearchParameters,
        processor: Processor<in VlangNamedElement>,
    ) {
        if (!parameter.isCheckDeep) return

        val method = parameter.element as? VlangInterfaceMethodDefinition ?: return
        val decl = method.getOwner()
        val interfaceType = decl.interfaceType
        if (!interfaceType.isValid) return

        VlangInheritorsSearch.processMethodOwners({ owner: VlangNamedElement ->
            val struct = owner as VlangStructDeclaration
            val name = method.name ?: return@processMethodOwners true
            val structMethod = struct.structType.toEx().findMethod(owner.project, name)

            structMethod == null || structMethod == method || processor.process(structMethod)
        }, decl, interfaceType, mutableListOf(method), listOf())
    }
}
