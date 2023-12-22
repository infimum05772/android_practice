package com.itis.android_tasks.service

import com.itis.android_tasks.model.dto.AnimeModel

interface FavoritesService {

    fun isFavorite(email: String, anime: AnimeModel) : Boolean

    fun addToFavorites(email: String, anime: AnimeModel)

    fun removeFromFavorites(email: String, anime: AnimeModel)
}
