package com.itis.android_tasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.data.db.entity.relation.UserFavorites

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorites(ref: UserFilmCrossRef)

    @Delete
    fun removeFromFavorites(ref: UserFilmCrossRef)

    @Query("SELECT * FROM favorites WHERE anime_id = :animeId AND user_id = :userId")
    fun getFavorite(userId: Int, animeId: Int): UserFilmCrossRef?
}
