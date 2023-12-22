package com.itis.android_tasks.model.rv

import com.itis.android_tasks.model.dto.AnimeModel

data class AnimeFeedElementModel(
    val animeModel: AnimeModel,
    var rating: Double
) : FeedElementModel
