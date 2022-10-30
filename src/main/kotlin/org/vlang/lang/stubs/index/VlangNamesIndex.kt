package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangNamedElement

class VlangNamesIndex : StringStubIndexExtension<VlangNamedElement>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangNamedElement>("vlang.named.element")

        fun find(
            fqn: String,
            project: Project,
            scope: GlobalSearchScope?,
        ): Collection<VlangNamedElement> {
            return StubIndex.getElements(KEY, fqn, project, scope, VlangNamedElement::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangNamedElement>,
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangNamedElement::class.java, processor
            )
        }

        fun getAllPrefix(prefix: String, project: Project): Collection<VlangNamedElement> {
            val keys = StubIndex.getInstance().getAllKeys(KEY, project)
            return keys.flatMap { key ->
                if (!key.startsWith(prefix)) {
                    return@flatMap emptyList()
                }

                StubIndex.getElements(KEY, key, project, null, null, VlangNamedElement::class.java)
            }
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 3

    override fun getKey() = KEY
}
