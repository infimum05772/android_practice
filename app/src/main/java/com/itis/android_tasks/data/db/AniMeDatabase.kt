package com.itis.android_tasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.android_tasks.data.db.dao.AnimeDao
import com.itis.android_tasks.data.db.dao.RatingDao
import com.itis.android_tasks.data.db.dao.UserDao
import com.itis.android_tasks.data.db.entity.AnimeEntity
import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.data.db.entity.RatingEntity
import com.itis.android_tasks.data.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        AnimeEntity::class,
        UserFilmCrossRef::class,
        RatingEntity::class
    ],
    version = 1
)
abstract class AniMeDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val animeDao: AnimeDao
    abstract val ratingDao: RatingDao

    companion object {
        const val DATABASE_NAME = "ani_me.db"
    }
}
