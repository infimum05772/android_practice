package com.itis.android_tasks.service

import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.model.dto.UserModel

interface UserService {

    fun getUserByEmail(email: String) : UserModel?

    fun isPhoneUnique(phone: String) : Boolean

    fun isEmailUnique(email: String) : Boolean

    fun isRegistered(email: String, password: String) : Boolean?

    fun updatePhone(email: String, phone: String) : Boolean

    fun updatePassword(email: String, oldPassword: String, newPassword: String) : Boolean

    fun saveUser(user: UserModel)

    fun deleteUser(email: String)
}
