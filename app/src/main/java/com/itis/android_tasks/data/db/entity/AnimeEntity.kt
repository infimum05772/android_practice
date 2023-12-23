package com.itis.android_tasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "anime_id")
    var animeId: Int,
    var name: String,
    var released: Int,
    var desc: String
)
