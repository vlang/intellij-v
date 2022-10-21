package org.vlang.ide.test

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.vfs.VirtualFile
import org.vlang.utils.isNotVlangFile
import org.vlang.utils.isTestFile

class VlangTestSourcesFilter : TestSourcesFilter() {
    override fun isTestSource(file: VirtualFile, project: Project): Boolean {
        if (!file.isInLocalFileSystem || file.isNotVlangFile) {
            return false
        }

        if (!ProjectFileIndex.getInstance(project).isInContent(file)) {
            return false
        }

        return file.isTestFile
    }
}
