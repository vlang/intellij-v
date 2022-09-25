package org.vlang.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.IndexableSetContributor
import org.vlang.sdk.VlangSdkUtil

class VlangIndexedRootsProvider : IndexableSetContributor() {
    override fun getAdditionalProjectRootsToIndex(project: Project): MutableSet<VirtualFile> {
        val sdk = VlangSdkUtil.getSdkSrcDir(project, null)
        val root = sdk?.findChild("vlib") ?: return mutableSetOf()
        return mutableSetOf(root)
    }

    override fun getAdditionalRootsToIndex(): MutableSet<VirtualFile> {
        return mutableSetOf()
    }
}