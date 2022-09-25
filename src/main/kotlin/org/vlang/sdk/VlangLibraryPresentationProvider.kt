package org.vlang.sdk

import com.intellij.openapi.roots.libraries.DummyLibraryProperties
import com.intellij.openapi.roots.libraries.LibraryKind
import com.intellij.openapi.roots.libraries.LibraryPresentationProvider
import com.intellij.openapi.vfs.VirtualFile

class VlangLibraryPresentationProvider : LibraryPresentationProvider<DummyLibraryProperties>(KIND) {
    companion object {
        private val KIND = LibraryKind.create("v")
    }

    override fun detect(classesRoots: MutableList<VirtualFile>): DummyLibraryProperties? {
        for (root in classesRoots) {
            if (!VlangSdkService.isSdkLibRoot(root)) {
                return DummyLibraryProperties.INSTANCE
            }
        }
        return null
    }
}
