package com.itis.android_tasks.service.impl

import com.itis.android_tasks.data.db.entity.UserEntity
import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.di.ServiceLocator
import com.itis.android_tasks.model.dto.AnimeModel
import com.itis.android_tasks.model.dto.UserModel
import com.itis.android_tasks.service.UserService
import com.itis.android_tasks.utils.PasswordUtil

object UserServiceImpl : UserService {

    override fun getUserByEmail(email: String): UserModel? {
        return ServiceLocator.getDBInstance().userDao.getUserByEmail(email)?.let {
            toUserModel(it)
        }
    }

    override fun isPhoneUnique(phone: String): Boolean {
        return ServiceLocator.getDBInstance().userDao.getUserByPhone(phone) == null
    }

    override fun isEmailUnique(email: String): Boolean {
        return ServiceLocator.getDBInstance().userDao.getUserByEmail(email) == null
    }


    override fun isRegistered(email: String, password: String): Boolean? {
        return ServiceLocator.getDBInstance().userDao.getUserByEmail(email)?.let {
            it.password == PasswordUtil.encrypt(password)
        }
    }

    override fun updatePhone(email: String, phone: String): Boolean {
        with(ServiceLocator.getDBInstance().userDao) {
            if (getUserByPhone(phone) == null) {
                updatePhone(email, phone)
                return true
            }
            return false
        }
    }

    override fun updatePassword(email: String, oldPassword: String, newPassword: String): Boolean {
        isRegistered(email, oldPassword)?.let {
            if (it) {
                ServiceLocator.getDBInstance().userDao.updatePassword(
                    email,
                    PasswordUtil.encrypt(newPassword)
                )
            }
            return it
        }
        return false
    }


    override fun saveUser(user: UserModel) {
        user.password = PasswordUtil.encrypt(user.password)
        ServiceLocator.getDBInstance().userDao.saveUser(toUserEntity(user, 0))
    }

    override fun deleteUser(email: String) {
        with(ServiceLocator.getDBInstance().userDao) {
            getUserByEmail(email)?.let {
                deleteUser(it)
            }
        }
    }

    private fun toUserEntity(userModel: UserModel, id: Int) = UserEntity(
        id,
        userModel.name,
        userModel.phone,
        userModel.email,
        userModel.password
    )

    private fun toUserModel(userEntity: UserEntity) = UserModel(
        userEntity.name,
        userEntity.phone,
        userEntity.email,
        userEntity.password
    )
}
