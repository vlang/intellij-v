package org.vlang.project

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.util.io.exists
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.utils.isNotVlangFile
import org.vlang.utils.toPath

class MissingToolchainNotificationProvider(project: Project) : VlangEditorNotificationProvider(project) {
    override fun createNotificationPanel(file: VirtualFile, editor: FileEditor, project: Project): EditorNotificationPanel? {
        if (file.isNotVlangFile || isNotificationDisabled(file)) {
            return null
        }

        val toolchainLocation = project.projectSettings.toolchainLocation
        if (toolchainLocation.isEmpty()) {
            return createToolchainNotification("V toolchain is not defined", project, file)
        }

        if (!toolchainLocation.toPath().exists()) {
            return createToolchainNotification("V toolchain folder $toolchainLocation is not exist", project, file)
        }

        return null
    }
}
