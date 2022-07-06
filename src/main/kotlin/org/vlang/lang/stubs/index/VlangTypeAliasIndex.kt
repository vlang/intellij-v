package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangTypeAliasDeclaration

class VlangTypeAliasIndex : StringStubIndexExtension<VlangTypeAliasDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangTypeAliasDeclaration>("vlang.type.alias")

        fun find(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?
        ): Collection<VlangTypeAliasDeclaration> {
            return StubIndex.getElements(KEY, name, project, scope, idFilter, VlangTypeAliasDeclaration::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangTypeAliasDeclaration>
        ): Boolean {

            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangTypeAliasDeclaration::class.java, processor
            )
        }

        fun getAll(project: Project): List<VlangTypeAliasDeclaration> {
            val result = mutableListOf<VlangTypeAliasDeclaration>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    null,
                    VlangTypeAliasDeclaration::class.java
                )
                result.addAll(els)
            }

            return result
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 3

    override fun getKey() = KEY
}
