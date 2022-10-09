package org.vlang.project

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.ui.EditorNotificationProvider.CONST_NULL
import com.intellij.ui.EditorNotifications
import org.vlang.configurations.VlangProjectSettingsConfigurable
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.utils.isNotVlangFile
import java.util.function.Function
import javax.swing.JComponent

class VlangEditorNotificationProvider : EditorNotificationProvider, DumbAware {
    override fun collectNotificationData(project: Project, file: VirtualFile): Function<in FileEditor, out JComponent> {
        if (file.isNotVlangFile) {
            return CONST_NULL
        }

        val toolchainLocation = project.projectSettings.toolchainLocation
        if (toolchainLocation.isNotEmpty()) {
            return CONST_NULL
        }

        return Function {
            EditorNotificationPanel().apply {
                text = "V toolchain is not defined"
                createActionLabel("Setup V toolchain") {
                    VlangProjectSettingsConfigurable.show(project)
                    update(project, file)
                }
            }
        }
    }

    private fun update(project: Project, file: VirtualFile) {
        EditorNotifications.getInstance(project).updateNotifications(file)
    }
}