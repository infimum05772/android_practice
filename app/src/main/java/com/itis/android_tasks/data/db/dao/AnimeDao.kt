package com.itis.android_tasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.android_tasks.data.db.entity.AnimeEntity

@Dao
interface AnimeDao {

    @Query("SELECT * FROM anime ORDER BY released DESC")
    fun getAllAnime(): List<AnimeEntity>?

    @Query("SELECT * FROM anime WHERE anime_id = :animeId")
    fun getAnimeById(animeId: Int) : AnimeEntity?

    @Query("SELECT * FROM anime WHERE name = :name AND released = :released")
    fun getAnimeByNameAndReleased(name: String, released: Int) : AnimeEntity?

    @Delete
    fun delete(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveAnime(anime: AnimeEntity)
}
