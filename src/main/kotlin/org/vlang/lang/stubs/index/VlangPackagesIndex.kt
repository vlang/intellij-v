package org.vlang.lang.stubs.index

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import org.vlang.lang.VlangFileElementType
import org.vlang.lang.psi.VlangFile

class VlangPackagesIndex : StringStubIndexExtension<VlangFile>() {
   companion object {
       val KEY = StubIndexKey.createIndexKey<String, VlangFile>("vlang.packages")
   }

    override fun getVersion() = VlangFileElementType.VERSION + 2

    override fun getKey() = KEY
}
