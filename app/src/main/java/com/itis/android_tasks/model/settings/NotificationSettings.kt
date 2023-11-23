package com.itis.android_tasks.model.settings

import com.itis.android_tasks.utils.NotificationImportance
import com.itis.android_tasks.utils.NotificationPrivacy

object NotificationSettings {
    var title: String = ""
    var description: String = ""
    var importance: NotificationImportance = NotificationImportance.MEDIUM
    var privacy: NotificationPrivacy = NotificationPrivacy.PUBLIC
    var isExpandableText: Boolean = false
    var isButtonsShowed: Boolean = false
}
