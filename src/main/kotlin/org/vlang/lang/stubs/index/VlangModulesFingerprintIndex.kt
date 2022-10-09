package org.vlang.lang.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFile

class VlangModulesFingerprintIndex : StringStubIndexExtension<VlangFile>() {
    companion object {
        val KEY = StubIndexKey.createIndexKey<String, VlangFile>("vlang.module.fingerprint")

        fun find(name: String, project: Project, scope: GlobalSearchScope?): Collection<VlangFile> {
            return StubIndex.getElements(KEY, name, project, scope, null, VlangFile::class.java)
        }

        fun getAll(project: Project): List<VlangFile> {
            val result = mutableListOf<VlangFile>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    null,
                    null,
                    VlangFile::class.java
                )
                result.addAll(els)
            }

            return result
        }

        fun getPrefixed(project: Project, prefix: String): List<VlangFile> {
            val result = mutableListOf<VlangFile>()
            for (key in StubIndex.getInstance().getAllKeys(KEY, project)) {
                if (!key.startsWith(prefix)) {
                    continue
                }

                val els = StubIndex.getElements(
                    KEY,
                    key,
                    project,
                    null,
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
