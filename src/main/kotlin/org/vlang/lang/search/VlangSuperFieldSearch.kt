package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration

class VlangSuperFieldSearch : QueryExecutorBase<VlangFieldDefinition, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in VlangFieldDefinition>
    ) {
        if (!parameters.isCheckDeep) return

        val field = parameters.element as? VlangFieldDefinition ?: return
        val fieldName = field.name ?: return
        val owner = field.getOwner() as? VlangStructDeclaration ?: return

        val processor = Processor<VlangNamedElement> { spec ->
            val interfaceDeclaration = spec as VlangInterfaceDeclaration
            val interfaceField = interfaceDeclaration.interfaceType.getFieldList().find { it.name == fieldName }

            interfaceField == null || interfaceField == field || consumer.process(interfaceField)
        }
        VlangSuperSearch.processFieldOwners(processor, owner, listOf(field))
    }

    companion object {
        val GO_SUPER_FIELD_SEARCH = VlangSuperFieldSearch()
    }
}
