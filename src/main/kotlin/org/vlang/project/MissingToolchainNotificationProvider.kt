package org.vlang.project

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.util.io.exists
import org.vlang.toolchain.VlangToolchain
import org.vlang.toolchain.VlangToolchainService.Companion.toolchainSettings
import org.vlang.utils.isNotVlangFile
import org.vlang.utils.toPath

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
        if (!homePath.toPath().exists()) {
            return createToolchainNotification("V toolchain folder $homePath is not exist", project, file)
        }

        return null
    }
}
