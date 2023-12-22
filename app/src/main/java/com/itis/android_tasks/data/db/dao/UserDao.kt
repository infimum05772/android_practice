package com.itis.android_tasks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.android_tasks.data.db.entity.UserEntity
import com.itis.android_tasks.data.db.entity.ref.UserFilmCrossRef
import com.itis.android_tasks.data.db.entity.relation.UserFavorites

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Int) : UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String) : UserEntity?

    @Query("SELECT * FROM users WHERE phone = :phone")
    fun getUserByPhone(phone: String) : UserEntity?

    @Query("UPDATE users SET phone = :phone WHERE email = :email")
    fun updatePhone(email: String, phone: String)

    @Query("UPDATE users SET password = :password WHERE email = :email")
    fun updatePassword(email: String, password: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorites(ref: UserFilmCrossRef)

    @Delete
    fun removeFromFavorites(ref: UserFilmCrossRef)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserFavoriteAnime(email: String) : UserFavorites?
}
