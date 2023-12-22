package com.itis.android_tasks.data.db.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.itis.android_tasks.data.db.entity.AnimeEntity
import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.data.db.entity.UserEntity

data class UserFavorites(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "anime_id",
        associateBy = Junction(UserFilmCrossRef::class)
    )
    val anime: List<AnimeEntity>
)
