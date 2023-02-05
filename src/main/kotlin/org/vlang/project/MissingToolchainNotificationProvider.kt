package org.vlang.project

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import org.vlang.toolchain.VlangToolchain
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import org.vlang.utils.isNotVlangFile
import org.vlang.utils.toPath
import java.nio.file.Files

class MissingToolchainNotificationProvider(project: Project) : VlangEditorNotificationProviderBase(project) {
    override fun createNotificationPanel(file: VirtualFile, editor: FileEditor, project: Project): EditorNotificationPanel? {
        if (file.isNotVlangFile || isNotificationDisabled()) {
            return null
        }

        val toolchain = project.toolchainSettings.toolchain()
        if (toolchain == VlangToolchain.NULL) {
            return createToolchainNotification("V toolchain is not defined", project, file)
        }

        val homePath = toolchain.homePath()
        if (!Files.exists(homePath.toPath())) {
            return createToolchainNotification("V toolchain folder $homePath is not exist", project, file)
        }

        return null
    }
}
