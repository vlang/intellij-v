package org.vlang.lang.usages

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiReference
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.UsageSearchContext
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangModuleClause
import org.vlang.lang.psi.impl.VlangModule

class VlangModuleClauseUsagesSearcher : QueryExecutorBase<PsiReference?, ReferencesSearch.SearchParameters>(true) {
    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference?>,
    ) {
        val moduleClause = queryParameters.elementToSearch as? VlangModuleClause ?: return
        val directory = moduleClause.containingFile.containingDirectory ?: return
        val packageName = moduleClause.name

        val module = VlangModule.fromDirectory(directory)
        val searchTarget = module.toPsi()

        val scopeDeterminedByUser = queryParameters.scopeDeterminedByUser
        val scope = if (scopeDeterminedByUser is LocalSearchScope || queryParameters.isIgnoreAccessScope)
            scopeDeterminedByUser
        else
            searchTarget.useScope

        queryParameters.optimizer.searchWord(
            packageName,
            scope,
            UsageSearchContext.IN_CODE,
            true,
            searchTarget,
        )
    }
}
