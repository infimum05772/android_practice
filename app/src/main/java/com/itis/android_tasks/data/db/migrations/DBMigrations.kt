package com.itis.android_tasks.data.db.migrations

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DBMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            try {
                db.execSQL("")
            } catch (ex: Exception) {
                Log.e("DB_MIGRATION", "Error occurred: $ex")
            }
        }
    }

}
