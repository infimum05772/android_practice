package com.itis.android_tasks.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.itis.android_tasks.MainActivity
import com.itis.android_tasks.R
import com.itis.android_tasks.model.settings.NotificationSettings

class NotificationHandler(private val ctx: Context) {

    private val notificationManager =
        ctx.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    private var mainIntent: PendingIntent? = null

    init {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        mainIntent = PendingIntent.getActivity(
            ctx,
            ParamsKey.INTENT_MAIN_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getDefaultBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            ctx,
            ParamsKey.DEFAULT_NOTIFICATION_CHANNEL_ID
                    + NotificationSettings.importance.name
        )
            .setSmallIcon(R.drawable.notification)
            .setAutoCancel(true)
            .setContentIntent(mainIntent)
            .setVisibility(NotificationSettings.privacy.visibility)
            .setPriority(NotificationSettings.importance.importance)
    }

    fun createCustomNotification() {
        notificationManager?.let { manager ->
            val notification = getDefaultBuilder()
                .setContentTitle(NotificationSettings.title)
                .also {
                    if (NotificationSettings.isExpandableText
                        && NotificationSettings.description.length >= MIN_LENGTH_FOR_EXPANDING) {
                        it.setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText(NotificationSettings.description)
                        )
                    } else {
                        it.setContentText(NotificationSettings.description)
                    }
                    if (NotificationSettings.isButtonsShowed) {
                        it.addAction(
                            R.drawable.icon_main,
                            ctx.getString(R.string.notification_action_home),
                            getHomeToastPendingIntent()
                        )
                        it.addAction(
                            R.drawable.icon_notifications_settings,
                            ctx.getString(R.string.notification_action_settings),
                            getNotificationSettingsPendingIntent()
                        )
                    }
                }

            manager.notify(CUSTOM_NOTIFICATION_ID, notification.build())
        }
    }

    private fun getHomeToastPendingIntent(): PendingIntent {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.putExtra(ParamsKey.INTENT_KEY, ParamsKey.INTENT_HOME_TOAST_VALUE)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            ctx,
            ParamsKey.INTENT_HOME_TOAST_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getNotificationSettingsPendingIntent(): PendingIntent {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.putExtra(ParamsKey.INTENT_KEY, ParamsKey.INTENT_NOTIFICATION_SETTINGS_VALUE)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            ctx,
            ParamsKey.INTENT_NOTIFICATION_SETTINGS_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createCoroutinesNotification() {
        notificationManager?.let { manager ->
            val notification = getDefaultBuilder()
                .setContentTitle(ctx.getString(R.string.coroutines_notification_title))
                .setContentText(ctx.getString(R.string.coroutines_notification_desc))

            manager.notify(COROUTINES_NOTIFICATION_ID, notification.build())
        }
    }

    companion object {
        const val CUSTOM_NOTIFICATION_ID = 1
        const val COROUTINES_NOTIFICATION_ID = 2
        private const val MIN_LENGTH_FOR_EXPANDING = 50
    }
}
