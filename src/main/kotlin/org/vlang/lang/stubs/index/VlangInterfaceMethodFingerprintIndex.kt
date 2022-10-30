package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangInterfaceMethodDefinition

class VlangInterfaceMethodFingerprintIndex : StringStubIndexExtension<VlangInterfaceMethodDefinition>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangInterfaceMethodDefinition>("vlang.interface.method.fingerprint")

        fun find(
            name: String, project: Project,
            scope: GlobalSearchScope?,
        ): Collection<VlangInterfaceMethodDefinition> {
            return StubIndex.getElements(KEY, name, project, scope, null, VlangInterfaceMethodDefinition::class.java)
        }

        fun process(
            name: String, project: Project,
            scope: GlobalSearchScope?, processor: Processor<VlangInterfaceMethodDefinition>,
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, null,
                VlangInterfaceMethodDefinition::class.java, processor
            )
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 2

    override fun getKey() = KEY
}
