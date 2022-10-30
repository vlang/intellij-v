package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.parentOfType
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement

class VlangSuperFieldSearch : QueryExecutorBase<VlangFieldDefinition, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in VlangFieldDefinition>
    ) {
        if (!parameters.isCheckDeep) return

        val field = parameters.element as? VlangFieldDefinition ?: return
        val owner = field.parentOfType<VlangNamedElement>() ?: return

        val processor = Processor<VlangNamedElement> { spec ->
            val iface = spec as VlangInterfaceDeclaration
            val name = field.name ?: return@Processor true
            val ifaceField = iface.interfaceType.getFieldList().find { it.name == name }

            ifaceField == null || field == ifaceField || consumer.process(ifaceField)
        }
        VlangSuperSearch.processFieldOwners(processor, owner, listOf(field))
    }

    companion object {
        val GO_SUPER_FIELD_SEARCH = VlangSuperFieldSearch()
    }
}
