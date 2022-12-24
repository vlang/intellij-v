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

abstract class VlangEditorNotificationProviderBase(private val project: Project) : EditorNotificationProvider, DumbAware {
    private val disablingKey: String get() = NOTIFICATION_STATUS_KEY

    final override fun collectNotificationData(
        project: Project,
        file: VirtualFile,
    ): Function<in FileEditor, out EditorNotificationPanel?> {
        return Function { editor -> createNotificationPanel(file, editor, project) }
    }

    protected abstract fun createNotificationPanel(
        file: VirtualFile,
        editor: FileEditor,
        project: Project,
    ): EditorNotificationPanel?

    protected fun createToolchainNotification(
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
                disableNotification()
                updateAllNotifications()
            }
        }

    protected fun createSimpleNotification(message: String): EditorNotificationPanel =
        EditorNotificationPanel().apply { text = message }

    private fun update(project: Project, file: VirtualFile) {
        EditorNotifications.getInstance(project).updateNotifications(file)
    }

    private fun updateAllNotifications() {
        EditorNotifications.getInstance(project).updateAllNotifications()
    }

    private fun disableNotification() {
        PropertiesComponent.getInstance(project).setValue(disablingKey, true)
    }

    protected fun isNotificationDisabled(): Boolean =
        PropertiesComponent.getInstance(project).getBoolean(disablingKey)

    companion object {
        private const val NOTIFICATION_STATUS_KEY = "org.vlang.hideToolchainNotifications"
    }
}
