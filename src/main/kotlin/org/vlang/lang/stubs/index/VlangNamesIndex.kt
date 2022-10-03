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
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?
        ): Collection<VlangNamedElement> {
            val res = StubIndex.getElements(KEY, fqn, project, scope, idFilter, VlangNamedElement::class.java)
            if (res.isNotEmpty()) {
                return res
            }

            val builtinName = "builtin.$name"

            return StubIndex.getElements(
                KEY,
                builtinName,
                project,
                scope,
                idFilter,
                VlangNamedElement::class.java
            )
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangNamedElement>
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangNamedElement::class.java, processor
            )
        }

        fun processPrefix(
            prefix: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangNamedElement>
        ): Boolean {
            val keys = StubIndex.getInstance().getAllKeys(KEY, project)
            for (key in keys) {
                if (!key.startsWith(prefix)) {
                    continue
                }

                val els = StubIndex.getElements(KEY, key, project, null, idFilter, VlangNamedElement::class.java)
                for (el in els) {
                    if (!processor.process(el)) {
                        return false
                    }
                }
            }

            return true
        }

        fun getAll(project: Project): List<VlangNamedElement> {
            val result = mutableListOf<VlangNamedElement>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    GlobalSearchScope.allScope(project),
                    null,
                    VlangNamedElement::class.java
                )
                result.addAll(els)
            }

            return result
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 3

    override fun getKey() = KEY
}
