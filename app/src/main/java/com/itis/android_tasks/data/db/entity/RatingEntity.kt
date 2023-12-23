package com.itis.android_tasks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "rating",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["anime_id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RatingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rating_id")
    var ratingId: Int,
    @ColumnInfo(name = "user_id") var userId: Int,
    @ColumnInfo(name = "anime_id") var animeId: Int,
    var value: Int
)
