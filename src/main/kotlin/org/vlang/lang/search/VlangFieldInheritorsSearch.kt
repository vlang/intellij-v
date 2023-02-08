package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration

object VlangFieldInheritorsSearch : QueryExecutorBase<VlangFieldDefinition, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameter: DefinitionsScopedSearch.SearchParameters,
        processor: Processor<in VlangFieldDefinition>
    ) {
        if (!parameter.isCheckDeep) return

        val field = parameter.element as? VlangFieldDefinition ?: return
        val decl = field.getOwner() as? VlangInterfaceDeclaration ?: return
        val interfaceType = decl.interfaceType

        VlangInheritorsSearch.processMethodOwners({ spec: VlangNamedElement ->
            val struct = spec as VlangStructDeclaration
            val structFields = struct.structType.fieldList

            val structField = structFields.find {
                val lhsType = it.getType(null)
                val rhsType = field.getType(null)
                it.name == field.name && lhsType != null && rhsType != null && lhsType.isEqual(rhsType)
            }

            structField == null || field == structField || processor.process(structField)
        }, decl, interfaceType, listOf(), mutableListOf(field))
    }
}
