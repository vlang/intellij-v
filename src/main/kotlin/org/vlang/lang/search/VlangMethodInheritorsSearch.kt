package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.Processor
import org.vlang.lang.psi.*
import org.vlang.lang.stubs.index.VlangNamesIndex

class VlangMethodInheritorsSearch : QueryExecutorBase<VlangSignatureOwner, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameter: DefinitionsScopedSearch.SearchParameters,
        processor: Processor<in VlangSignatureOwner>
    ) {
        if (!parameter.isCheckDeep) return

        val method = parameter.element as? VlangInterfaceMethodDefinition ?: return
        val interfaceType = PsiTreeUtil.getStubOrPsiParentOfType(method, VlangInterfaceType::class.java)
        val decl = interfaceType?.parentOfType<VlangInterfaceDeclaration>() ?: return
        if (!interfaceType.isValid) return

        VlangInheritorsSearch().processTypeSpec(parameter, { spec: VlangNamedElement ->
            val struct = spec as VlangStructDeclaration
            val name = struct.getQualifiedName() ?: return@processTypeSpec true
            val fqn = name + "." + method.name
            val structMethod = VlangNamesIndex.find(fqn, "", method.project, null) as? VlangMethodDeclaration

//            val structMethod = structMethods.find {
//                val lhsTypeEx = it.getType(null).toEx()
//                val rhsTypeEx = method.getType(null).toEx()
//                it.name == method.name && lhsTypeEx.isEqual(rhsTypeEx)
//            }

            structMethod == null || structMethod === method || processor.process(structMethod)
        }, decl, interfaceType, mutableListOf(method), listOf(), method.isPublic())
    }
}
