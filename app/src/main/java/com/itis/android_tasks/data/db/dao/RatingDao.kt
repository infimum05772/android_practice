package com.itis.android_tasks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.android_tasks.data.db.entity.RatingEntity

@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRating(rating: RatingEntity)

    @Query("SELECT * FROM rating WHERE anime_id = :animeId")
    fun getAnimeRatingByAllUsers(animeId: Int): List<RatingEntity>?

    @Query("SELECT * FROM rating WHERE anime_id = :animeId AND user_id = :userId")
    fun getRatingByAnimeAndUser(animeId: Int, userId: Int): RatingEntity?
}
