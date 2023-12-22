package com.itis.android_tasks.service.impl

import com.itis.android_tasks.data.db.entity.RatingEntity
import com.itis.android_tasks.di.ServiceLocator
import com.itis.android_tasks.model.AnimeModel
import com.itis.android_tasks.model.RatingModel
import com.itis.android_tasks.service.RatingService

object RatingServiceImpl : RatingService {
    override fun addRating(rating: RatingModel) {
        with(ServiceLocator.getDBInstance()) {
            val userId = userDao.getUserByEmail(rating.email)?.userId
            val animeId =
                animeDao.getAnimeByNameAndReleased(rating.animeName, rating.animeReleased)?.animeId
            if (userId != null && animeId != null) {
                ratingDao.addRating(
                    RatingEntity(
                        0,
                        userId,
                        animeId,
                        rating.value
                    )
                )
            }
        }
    }

    override fun getAnimeRatingByAllUsers(anime: AnimeModel): Double {
        with(ServiceLocator.getDBInstance()) {
            animeDao.getAnimeByNameAndReleased(anime.name, anime.released)?.animeId?.let {animeId ->
                ratingDao.getAnimeRatingByAllUsers(animeId)?.stream()?.mapToInt { rating ->
                    rating.value
                }?.average()?.let { avg ->
                    return avg.orElse(0.0)
                }
            }
        }
        return 0.0
    }

    override fun getRatingByAnimeAndUser(anime: AnimeModel, email: String): Int {
        with(ServiceLocator.getDBInstance()) {
            val userId = userDao.getUserByEmail(email)?.userId
            val animeId =
                animeDao.getAnimeByNameAndReleased(anime.name, anime.released)?.animeId
            if (userId != null && animeId != null) {
                return ratingDao.getRatingByAnimeAndUser(animeId, userId).let {
                    it?.value ?: 0
                }
            }
        }
        return 0
    }

}
