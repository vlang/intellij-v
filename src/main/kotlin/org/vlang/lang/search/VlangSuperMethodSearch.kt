package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangInterfaceMethodDefinition
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.isNullableEqual
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangStructTypeEx

object VlangSuperMethodSearch : QueryExecutorBase<VlangInterfaceMethodDefinition, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in VlangInterfaceMethodDefinition>,
    ) {
        if (!parameters.isCheckDeep) return

        val method = parameters.element as? VlangMethodDeclaration ?: return
        val methodName = method.name ?: return

        val receiverType = method.receiverType.toEx().unwrapPointer()
        if (receiverType !is VlangStructTypeEx) return
        val structDecl = receiverType.resolve(parameters.project) ?: return

        VlangSuperSearch.processMethodOwners({ interfaceDeclaration ->
            val iface = interfaceDeclaration as VlangInterfaceDeclaration
            val interfaceMethod = iface
                .interfaceType
                .methodList
                .find { it.name == methodName && it.getType(null).isNullableEqual(method.getType(null)) }

            interfaceMethod == null || method == interfaceMethod || consumer.process(interfaceMethod)
        }, structDecl, listOf(method))
    }
}
