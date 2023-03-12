package org.vlang.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.notification.impl.NotificationFullContent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import java.util.function.BiConsumer

open class VlangNotification(
    content: String = "",
    type: NotificationType = NotificationType.INFORMATION,
) : Notification(ID, content, type), NotificationFullContent {

    companion object {
        const val ID = "V Plugin"
        private val LOG = logger<VlangNotification>()
    }

    fun withActions(vararg actions: NotificationAction): VlangNotification {
        actions.forEach {
            addAction(it)
        }

        return this
    }

    fun withTitle(title: String): VlangNotification {
        setTitle(title)
        return this
    }

    fun show(project: Project? = null) {
        invokeLater {
            Notifications.Bus.notify(this, project)
            LOG.info("Notification: title: $title, content: ${content.ifEmpty { "<empty>" }}, type: $type")
        }
    }

    class Action(msg: String, private val runnable: BiConsumer<AnActionEvent, Notification>) : NotificationAction(msg) {
        override fun actionPerformed(e: AnActionEvent, notification: Notification) {
            runnable.accept(e, notification)
        }
    }
}
