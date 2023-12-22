package com.itis.android_tasks.service

import com.itis.android_tasks.data.db.entity.UserEntity
import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.data.db.entity.relation.UserFavorites
import com.itis.android_tasks.model.AnimeModel
import com.itis.android_tasks.model.UserModel

interface UserService {

    fun getUserByEmail(email: String) : UserModel?

    fun isPhoneUnique(phone: String) : Boolean

    fun isEmailUnique(email: String) : Boolean

    fun isRegistered(email: String, password: String) : Boolean?

    fun updatePhone(email: String, phone: String) : Boolean

    fun updatePassword(email: String, oldPassword: String, newPassword: String) : Boolean

    fun saveUser(user: UserModel)

    fun deleteUser(email: String)

    fun addToFavorites(email: String, anime: AnimeModel)

    fun removeFromFavorites(email: String, anime: AnimeModel)

    fun getUserFavoriteAnime(email: String) : List<AnimeModel>?
}
