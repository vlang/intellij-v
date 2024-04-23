package io.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import io.vlang.lang.psi.VlangInterfaceDeclaration
import io.vlang.lang.psi.VlangInterfaceMethodDefinition
import io.vlang.lang.psi.VlangMethodDeclaration
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.isNullableEqual
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import io.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import io.vlang.lang.psi.types.VlangStructTypeEx

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
