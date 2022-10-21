package org.vlang.project

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import com.intellij.ui.EditorNotifications
import org.vlang.configurations.VlangProjectSettingsConfigurable
import java.util.function.Function

abstract class VlangEditorNotificationProvider(private val project: Project) : EditorNotificationProvider, DumbAware {
    val disablingKey: String get() = NOTIFICATION_STATUS_KEY

    final override fun collectNotificationData(
        project: Project,
        file: VirtualFile
    ): Function<in FileEditor, out EditorNotificationPanel?> {
        return Function { editor -> createNotificationPanel(file, editor, project) }
    }

    protected abstract fun createNotificationPanel(
        file: VirtualFile,
        editor: FileEditor,
        project: Project
    ): EditorNotificationPanel?

    protected fun createNotification(
        message: String,
        project: Project,
        file: VirtualFile,
    ): EditorNotificationPanel =
        EditorNotificationPanel().apply {
            text = message
            createActionLabel("Setup V toolchain") {
                VlangProjectSettingsConfigurable.show(project)
                update(project, file)
            }
            createActionLabel("Don't show again") {
                disableNotification(file)
                updateAllNotifications()
            }
        }


    private fun update(project: Project, file: VirtualFile) {
        EditorNotifications.getInstance(project).updateNotifications(file)
    }

    protected fun updateAllNotifications() {
        EditorNotifications.getInstance(project).updateAllNotifications()
    }

    protected fun disableNotification(file: VirtualFile) {
        PropertiesComponent.getInstance(project).setValue(disablingKey, true)
    }

    protected fun isNotificationDisabled(file: VirtualFile): Boolean =
        PropertiesComponent.getInstance(project).getBoolean(disablingKey)

    companion object {
        private const val NOTIFICATION_STATUS_KEY = "org.vlang.hideToolchainNotifications"
    }
}
