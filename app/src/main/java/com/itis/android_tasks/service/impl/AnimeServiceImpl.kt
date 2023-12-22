package com.itis.android_tasks.service.impl

import com.itis.android_tasks.data.db.entity.AnimeEntity
import com.itis.android_tasks.di.ServiceLocator
import com.itis.android_tasks.model.AnimeModel
import com.itis.android_tasks.service.AnimeService

object AnimeServiceImpl : AnimeService {

    override fun getAllAnime(): List<AnimeModel>? {
        return ServiceLocator.getDBInstance().animeDao.getAllAnime()?.map { animeEntity ->
            toAnimeModel(animeEntity)
        }
    }

    override fun getAnimeByNameAndReleased(name: String, released: Int): AnimeModel? {
        return ServiceLocator.getDBInstance().animeDao.getAnimeByNameAndReleased(name, released)
            ?.let {
                toAnimeModel(it)
            }
    }

    override fun isAnimeUnique(name: String, released: Int): Boolean {
        return ServiceLocator.getDBInstance().animeDao.getAnimeByNameAndReleased(
            name,
            released
        ) == null
    }

    override fun delete(anime: AnimeModel) {
        ServiceLocator.getDBInstance().animeDao.getAnimeByNameAndReleased(
            anime.name,
            anime.released
        )?.animeId?.let {
            ServiceLocator.getDBInstance().animeDao.delete(toAnimeEntity(anime, it))
        }
    }

    override fun saveAnime(anime: AnimeModel) {
        ServiceLocator.getDBInstance().animeDao.saveAnime(toAnimeEntity(anime, 0))
    }

    override fun getUserFavoriteAnime(email: String): List<AnimeModel>? {
        return ServiceLocator.getDBInstance().userDao.getUserFavoriteAnime(email)?.anime?.map { animeEntity ->
            toAnimeModel(
                animeEntity
            )
        }
    }

    private fun toAnimeEntity(animeModel: AnimeModel, id: Int) = AnimeEntity(
        id,
        animeModel.name,
        animeModel.released,
        animeModel.desc
    )


    private fun toAnimeModel(animeEntity: AnimeEntity) = AnimeModel(
        animeEntity.name,
        animeEntity.released,
        animeEntity.desc
    )

}
