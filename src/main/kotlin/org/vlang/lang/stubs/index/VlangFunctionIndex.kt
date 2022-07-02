package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFunctionDeclaration

class VlangFunctionIndex : StringStubIndexExtension<VlangFunctionDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangFunctionDeclaration>("vlang.function")

        fun find(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?
        ): Collection<VlangFunctionDeclaration?> {
            return StubIndex.getElements(KEY, name, project, scope, idFilter, VlangFunctionDeclaration::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangFunctionDeclaration?>
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangFunctionDeclaration::class.java, processor
            )
        }
    }

    override fun getVersion(): Int {
        return VlangFileElementType.VERSION + 3
    }

    override fun getKey() = KEY
}
