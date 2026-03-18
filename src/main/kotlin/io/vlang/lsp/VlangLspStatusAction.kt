package io.vlang.lsp

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager

/**
 * Diagnostic action: reports whether the V language server is currently detected
 * for the file open in the editor. Accessible via Help menu or Find Action.
 */
class VlangLspStatusAction : AnAction("Check V Language Server Status") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = FileEditorManager.getInstance(project).selectedEditor?.file

        val bridge = project.service<VlangLspBridge>()
        val bridgeClass = bridge::class.simpleName

        val status = when {
            file == null -> "No file open in editor.\nBridge: $bridgeClass"
            bridge.isActiveForFile(file, project) ->
                "LSP ACTIVE — a V language server is registered for '${file.name}'.\n" +
                "Bridge: $bridgeClass"
            else ->
                "LSP NOT ACTIVE — no V language server detected for '${file.name}'.\n" +
                "Bridge: $bridgeClass"
        }

        NotificationGroupManager.getInstance()
            .getNotificationGroup("V Plugin")
            .createNotification("V Language Server Detection", status, NotificationType.INFORMATION)
            .notify(project)
    }
}
