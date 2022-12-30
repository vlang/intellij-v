package org.vlang.ide.navigation.goto

import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.GotoClassContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.ArrayUtil
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.psi.VlangNamedElement

open class VlangGotoContributorBase<T : VlangNamedElement>(
    private val clazz: Class<T>,
    private vararg val indexKeys: StubIndexKey<String, T>,
) : GotoClassContributor, ChooseByNameContributorEx {

    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        for (key in indexKeys) {
            ProgressManager.checkCanceled()
            StubIndex.getInstance().processAllKeys(key, processor, scope, filter)
        }
    }

    override fun processElementsWithName(name: String, processor: Processor<in NavigationItem>, parameters: FindSymbolParameters) {
        for (key in indexKeys) {
            ProgressManager.checkCanceled()
            StubIndex.getInstance().processElements(
                key, name, parameters.project, parameters.searchScope, parameters.idFilter,
                clazz, processor
            )
        }
    }

    override fun getQualifiedName(item: NavigationItem): String? {
        return if (item is VlangNamedElement) item.getQualifiedName() else null
    }

    override fun getQualifiedNameSeparator() = "."

    override fun getNames(p: Project, i: Boolean): Array<String> = ArrayUtil.EMPTY_STRING_ARRAY

    override fun getItemsByName(n: String, p: String, p1: Project, i: Boolean): Array<NavigationItem> =
        NavigationItem.EMPTY_NAVIGATION_ITEM_ARRAY
}
