package io.vlang.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Detects whether a V language server (via LSP4IJ) is active for a given file.
 *
 * The default implementation [NoOpLspBridge] always returns false.
 * When LSP4IJ is installed, [Lsp4ijBridge] is loaded via the optional dependency
 * declared in vlang-lsp4ij.xml and overrides this service.
 */
interface VlangLspBridge {
    fun isActiveForFile(file: VirtualFile, project: Project): Boolean
}
