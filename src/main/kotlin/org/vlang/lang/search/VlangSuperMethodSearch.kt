package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangInterfaceMethodDeclaration
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement

class VlangSuperMethodSearch : QueryExecutorBase<VlangInterfaceMethodDeclaration, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in VlangInterfaceMethodDeclaration>
    ) {
        if (!parameters.isCheckDeep) return

        val method = parameters.element as? VlangMethodDeclaration ?: return
        val owner = method.receiverType?.typeReferenceExpression?.resolve() as? VlangNamedElement ?: return
        val processor = Processor<VlangNamedElement> { spec ->
            val iface = spec as VlangInterfaceDeclaration
            val name = method.name ?: return@Processor true
            val ifaceMethod = iface.interfaceType.methodList.find { it.name == name }?.parent as? VlangInterfaceMethodDeclaration

            ifaceMethod == null || method == ifaceMethod || consumer.process(ifaceMethod)
        }
        VlangSuperSearch.processMethodOwners(processor, owner, listOf(method))
    }

    companion object {
        val GO_SUPER_METHOD_SEARCH = VlangSuperMethodSearch()
    }
}
