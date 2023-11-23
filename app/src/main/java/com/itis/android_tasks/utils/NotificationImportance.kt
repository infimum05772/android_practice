package com.itis.android_tasks.utils

import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat

enum class NotificationImportance(val importance: Int) {
    MEDIUM(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationManager.IMPORTANCE_LOW else NotificationCompat.PRIORITY_LOW),
    HIGH(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationManager.IMPORTANCE_DEFAULT else NotificationCompat.PRIORITY_DEFAULT),
    URGENT(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationManager.IMPORTANCE_HIGH else NotificationCompat.PRIORITY_DEFAULT)
}
