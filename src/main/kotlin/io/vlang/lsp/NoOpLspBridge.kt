package io.vlang.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Default bridge used when LSP4IJ is not installed.
 * Always reports no active language server.
 */
class NoOpLspBridge : VlangLspBridge {
    override fun isActiveForFile(file: VirtualFile, project: Project) = false
}
