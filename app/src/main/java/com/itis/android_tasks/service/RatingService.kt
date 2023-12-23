package com.itis.android_tasks.service

import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.model.dto.RatingModel

interface RatingService {

    fun setRating(rating: RatingModel)

    fun getAnimeRatingByAllUsers(anime: AnimeModel): Double

    fun getRatingByAnimeAndUser(anime: AnimeModel, email: String): Int
}
