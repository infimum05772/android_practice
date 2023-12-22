package com.itis.android_tasks.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.itis.android_tasks.data.db.AniMeDatabase
import com.itis.android_tasks.utils.ParamsKey

object ServiceLocator {

    private var dbInstance: AniMeDatabase? = null

    private var prefs: SharedPreferences? = null

    fun createData(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, AniMeDatabase::class.java, AniMeDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
        prefs = ctx.getSharedPreferences(ParamsKey.APP_CONFIG, Context.MODE_PRIVATE)
    }

    fun getDBInstance(): AniMeDatabase {
        return dbInstance ?: throw NullPointerException("DB not initialized")
    }

    fun getSharedPreferences(): SharedPreferences {
        return prefs ?: throw NullPointerException("Preferences not initialized")
    }
}
