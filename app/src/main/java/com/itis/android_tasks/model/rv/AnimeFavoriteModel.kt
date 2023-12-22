package com.itis.android_tasks.model.rv

import com.itis.android_tasks.model.dto.AnimeModel

data class AnimeFavoriteModel(
    val animeModel: AnimeModel,
    var rating: Double
)
