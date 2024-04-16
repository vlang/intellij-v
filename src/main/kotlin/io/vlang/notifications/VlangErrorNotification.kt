package io.vlang.notifications

import com.intellij.notification.NotificationType

class VlangErrorNotification(content: String = "") :
    VlangNotification(content, NotificationType.ERROR)
