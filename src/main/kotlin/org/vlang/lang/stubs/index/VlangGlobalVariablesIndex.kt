package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import com.intellij.util.indexing.IdFilter
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangGlobalVariableDefinition

class VlangGlobalVariablesIndex : StringStubIndexExtension<VlangGlobalVariableDefinition>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangGlobalVariableDefinition>("vlang.global.variables")

        fun find(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?
        ): Collection<VlangGlobalVariableDefinition> {
            return StubIndex.getElements(KEY, name, project, scope, idFilter, VlangGlobalVariableDefinition::class.java)
        }

        fun process(
            name: String,
            project: Project,
            scope: GlobalSearchScope?,
            idFilter: IdFilter?,
            processor: Processor<VlangGlobalVariableDefinition>
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, idFilter,
                VlangGlobalVariableDefinition::class.java, processor
            )
        }

        fun getAll(project: Project, keyFilter: (String) -> Boolean): List<VlangGlobalVariableDefinition> {
            val result = mutableListOf<VlangGlobalVariableDefinition>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                if (!keyFilter(key)) {
                    continue
                }
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    null,
                    null,
                    VlangGlobalVariableDefinition::class.java
                )
                result.addAll(els)
            }

            return result
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 3

    override fun getKey() = KEY
}
