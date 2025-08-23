package io.vlang.ide.run

import com.intellij.execution.filters.ConsoleFilterProviderEx
import com.intellij.execution.filters.Filter
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope

class VlangConsoleFilterProvider : ConsoleFilterProviderEx {
    override fun getDefaultFilters(
        project: Project,
        scope: GlobalSearchScope,
    ): Array<out Filter> =
        arrayOf(VlangConsoleFilter(project, GlobalSearchScope.allScope(project)))

    override fun getDefaultFilters(project: Project): Array<out Filter> =
        getDefaultFilters(project, GlobalSearchScope.allScope(project))
}