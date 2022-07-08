package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangEnumDeclaration

class VlangEnumIndex : StringStubIndexExtension<VlangEnumDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangEnumDeclaration>("vlang.enum")

        fun find(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?
        ): Collection<VlangEnumDeclaration> {
            return StubIndex.getElements(KEY, name, project, scope, idFilter, VlangEnumDeclaration::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangEnumDeclaration>
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangEnumDeclaration::class.java, processor
            )
        }

        fun getAll(project: Project): List<VlangEnumDeclaration> {
            val result = mutableListOf<VlangEnumDeclaration>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    null,
                    VlangEnumDeclaration::class.java
                )
                result.addAll(els)
            }

            return result
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 3

    override fun getKey() = KEY
}
