package com.itis.android_tasks.data.db.entity.ref

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.itis.android_tasks.data.db.entity.AnimeEntity
import com.itis.android_tasks.data.db.entity.UserEntity

@Entity(
    tableName = "favorites",
    primaryKeys = ["user_id", "anime_id"],
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
data class UserFilmCrossRef(
    @ColumnInfo(name = "user_id") var userId: Int,
    @ColumnInfo(name = "anime_id") var animeId: Int
)
