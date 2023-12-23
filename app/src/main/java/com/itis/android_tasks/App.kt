package com.itis.android_tasks

import android.app.Application
import com.itis.android_tasks.di.ServiceLocator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.createData(this)
    }
}
