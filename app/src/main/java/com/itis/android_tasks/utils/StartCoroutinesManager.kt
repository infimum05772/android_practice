package com.itis.android_tasks.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.itis.android_tasks.R
import com.itis.android_tasks.model.settings.CoroutinesSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartCoroutinesManager(
    private val activity: AppCompatActivity,
    coroutineSettings: CoroutinesSettings
) {

    private val count = coroutineSettings.count
    private val isAsync = coroutineSettings.isAsync
    val isStoppedOnBackground = coroutineSettings.isStoppedOnBackground
    private var cancelledCoroutinesCount: Int = count
    private val notificationHandler = NotificationHandler(activity)

    fun startCoroutines(): Job {
        return activity.lifecycleScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    repeat(count) {
                        if (!isAsync) {
                            doCoroutineTask()
                        } else {
                            launch {
                                doCoroutineTask()
                            }
                        }
                        cancelledCoroutinesCount--
                    }
                }
            }.onSuccess {
                notificationHandler.createCoroutinesNotification()
            }.onFailure {
                Log.i(
                    activity.localClassName,
                    activity.getString(R.string.cancelled_coroutines_count_log, cancelledCoroutinesCount)
                )
            }
        }
    }

    private suspend fun doCoroutineTask() {
        delay(1000)
    }
}
