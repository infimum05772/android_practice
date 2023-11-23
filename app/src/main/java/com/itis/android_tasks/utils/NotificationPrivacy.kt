package com.itis.android_tasks.utils

import androidx.core.app.NotificationCompat

enum class NotificationPrivacy(val visibility: Int) {
    PUBLIC(NotificationCompat.VISIBILITY_PUBLIC),
    SECRET(NotificationCompat.VISIBILITY_SECRET),
    PRIVATE(NotificationCompat.VISIBILITY_PRIVATE)
}
