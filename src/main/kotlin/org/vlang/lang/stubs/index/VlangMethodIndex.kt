package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement

class VlangMethodIndex : StringStubIndexExtension<VlangMethodDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangMethodDeclaration>("vlang.method")
        
        fun find(
            name: String, project: Project,
            scope: GlobalSearchScope?, idFilter: IdFilter?
        ): Collection<VlangMethodDeclaration> {
            return StubIndex.getElements(KEY, name, project, scope, idFilter, VlangMethodDeclaration::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangMethodDeclaration>
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangMethodDeclaration::class.java, processor
            )
        }

        fun processPrefix(
            prefix: String,
            project: Project,
            scope: GlobalSearchScope?,
            processor: Processor<VlangNamedElement>,
        ): Boolean {
            val keys = StubIndex.getInstance().getAllKeys(VlangNamesIndex.KEY, project)
            for (key in keys) {
                if (!key.startsWith(prefix)) {
                    continue
                }

                val els = StubIndex.getElements(VlangNamesIndex.KEY, key, project, null, null, VlangNamedElement::class.java)
                for (el in els) {
                    if (!processor.process(el)) {
                        return false
                    }
                }
            }

            return true
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 2

    override fun getKey() = KEY
}
