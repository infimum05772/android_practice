package com.itis.android_tasks.utils

import android.app.NotificationManager

enum class NotificationImportance(val importance: Int) {
    MEDIUM(NotificationManager.IMPORTANCE_LOW),
    HIGH(NotificationManager.IMPORTANCE_DEFAULT),
    URGENT(NotificationManager.IMPORTANCE_HIGH)
}
