package com.itis.android_tasks.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings

class AirplaneModeChangingListener(
    context: Context,
    private val onAirplaneModeChanged:  ((Boolean) -> Unit)? = null,
)  {

    private var isAirplaneMode: Boolean = Settings.Global.getInt(
        context.contentResolver,
        Settings.Global.AIRPLANE_MODE_ON,
        0
    ) != 0

    var receiver: BroadcastReceiver? = null

    init {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                isAirplaneMode = intent?.getBooleanExtra(STATE_KEY, false) == true
                onAirplaneModeChanged?.invoke(isAirplaneMode)
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    fun onStartAirplaneModeCheck() {
        onAirplaneModeChanged?.invoke(isAirplaneMode)
    }

    companion object {
        private const val STATE_KEY = "state"
    }
}
