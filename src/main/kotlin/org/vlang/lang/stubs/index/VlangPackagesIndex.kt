package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFile

class VlangPackagesIndex : StringStubIndexExtension<VlangFile>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangFile>("vlang.packages")

        fun find(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
        ): Collection<VlangFile> {
            val result = mutableListOf<VlangFile>()
            val keys = StubIndex.getInstance().getAllKeys(KEY, project).toSet().filter { it == name }

            for (key in keys) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    idFilter,
                    VlangFile::class.java
                )
                result.addAll(els)
            }

            return result
//            return StubIndex.getElements(KEY, name, project, scope, idFilter, VlangFile::class.java)
        }

        fun getAll(project: Project): List<VlangFile> {
            val result = mutableListOf<VlangFile>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    null,
                    VlangFile::class.java
                )
                result.addAll(els)
            }

            return result
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 2

    override fun getKey() = KEY
}
