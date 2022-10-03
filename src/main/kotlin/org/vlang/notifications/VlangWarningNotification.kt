package org.vlang.notifications

import com.intellij.notification.NotificationType

class VlangWarningNotification(content: String = "") :
    VlangNotification(content, NotificationType.WARNING)
