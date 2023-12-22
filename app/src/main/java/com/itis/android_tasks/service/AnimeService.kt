package com.itis.android_tasks.service

import com.itis.android_tasks.model.AnimeModel

interface AnimeService {

    fun getAllAnime(): List<AnimeModel>?

    fun getAnimeByNameAndReleased(name: String, released: Int) : AnimeModel?

    fun delete(anime: AnimeModel)

    fun saveAnime(anime: AnimeModel)

}
