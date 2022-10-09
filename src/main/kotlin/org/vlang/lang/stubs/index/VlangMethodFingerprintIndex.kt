package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangMethodDeclaration

class VlangMethodFingerprintIndex : StringStubIndexExtension<VlangMethodDeclaration>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangMethodDeclaration>("vlang.method.fingerprint")

        fun find(
            name: String, project: Project,
            scope: GlobalSearchScope?,
        ): Collection<VlangMethodDeclaration> {
            return StubIndex.getElements(KEY, name, project, scope, null, VlangMethodDeclaration::class.java)
        }

        fun process(
            name: String, project: Project,
            scope: GlobalSearchScope?, processor: Processor<VlangMethodDeclaration>,
        ): Boolean {
            return StubIndex.getInstance().processElements(
                KEY, name, project, scope, null,
                VlangMethodDeclaration::class.java, processor
            )
        }
    }

    override fun getVersion() = VlangFileElementType.VERSION + 2

    override fun getKey() = KEY
}
