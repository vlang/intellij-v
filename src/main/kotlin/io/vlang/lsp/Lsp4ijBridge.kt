package io.vlang.lsp

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.redhat.devtools.lsp4ij.LanguageServersRegistry

/**
 * LSP4IJ-aware bridge. Loaded only when the LSP4IJ plugin (com.redhat.devtools.lsp4ij)
 * is installed. Registered via vlang-lsp4ij.xml as a service override.
 *
 * Uses [LanguageServersRegistry.isFileSupported] which is synchronous and performs
 * no network I/O — it checks only the in-memory registry of configured servers.
 */
class Lsp4ijBridge : VlangLspBridge {
    override fun isActiveForFile(file: VirtualFile, project: Project): Boolean {
        return try {
            val active = LanguageServersRegistry.getInstance().isFileSupported(file, project)
            if (active) {
                LOG.debug("V language server detected for ${file.name} — duplicate feature suppression is available")
            }
            active
        } catch (e: Exception) {
            LOG.warn("LSP4IJ bridge: unexpected error checking language server status", e)
            false
        }
    }

    companion object {
        private val LOG = logger<Lsp4ijBridge>()
    }
}
