package com.itis.android_tasks.session

import com.itis.android_tasks.di.ServiceLocator
import com.itis.android_tasks.model.dto.UserModel
import com.itis.android_tasks.service.impl.UserServiceImpl
import com.itis.android_tasks.utils.ParamsKey

object AppSession {
    private var currentUser: UserModel? = null

    fun init(email: String) {
        currentUser = UserServiceImpl.getUserByEmail(email)
    }

    fun updateUser() {
        currentUser?.let {
            currentUser = UserServiceImpl.getUserByEmail(it.email)
        }
    }

    fun getCurrentUser() : UserModel? {
        return currentUser
    }

    fun removeUser() {
        currentUser = null
    }

    fun saveSession() {
        currentUser.let {
            ServiceLocator.getSharedPreferences().edit().apply {
                putString(ParamsKey.EMAIL_SP_KEY, it?.email)
                apply()
            }
        }
    }
}
