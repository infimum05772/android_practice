package com.itis.android_tasks.service.impl

import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.di.ServiceLocator
import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.service.FavoritesService

object FavoritesServiceImpl : FavoritesService {

    override fun isFavorite(email: String, anime: AnimeModel): Boolean {
        with(ServiceLocator.getDBInstance()) {
            val userId = userDao.getUserByEmail(email)?.userId
            val animeId = animeDao.getAnimeByNameAndReleased(anime.name, anime.released)?.animeId
            if (userId != null && animeId != null) {
                return favoritesDao.getFavorite(
                        userId,
                        animeId
                ) != null
            }
        }
        return false
    }

    override fun addToFavorites(email: String, anime: AnimeModel) {
        with(ServiceLocator.getDBInstance()) {
            val userId = userDao.getUserByEmail(email)?.userId
            val animeId = animeDao.getAnimeByNameAndReleased(anime.name, anime.released)?.animeId
            if (userId != null && animeId != null) {
                favoritesDao.addToFavorites(
                    UserFilmCrossRef(
                        userId,
                        animeId
                    )
                )
            }
        }
    }

    override fun removeFromFavorites(email: String, anime: AnimeModel) {
        with(ServiceLocator.getDBInstance()) {
            val userId = userDao.getUserByEmail(email)?.userId
            val animeId = animeDao.getAnimeByNameAndReleased(anime.name, anime.released)?.animeId
            if (userId != null && animeId != null) {
                favoritesDao.removeFromFavorites(
                    UserFilmCrossRef(
                        userId,
                        animeId
                    )
                )
            }
        }
    }
}
