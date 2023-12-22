package com.itis.android_tasks.service

import com.itis.android_tasks.model.AnimeModel
import com.itis.android_tasks.model.RatingModel

interface RatingService {

    fun addRating(rating: RatingModel)

    fun getAnimeRatingByAllUsers(anime: AnimeModel): Double

    fun getRatingByAnimeAndUser(anime: AnimeModel, email: String): Int
}
