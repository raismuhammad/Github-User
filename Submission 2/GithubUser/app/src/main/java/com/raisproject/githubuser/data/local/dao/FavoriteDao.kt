package com.raisproject.githubuser.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.raisproject.githubuser.data.local.entity.FavoriteEntity
import com.raisproject.githubuser.model.User

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorite(): List<User>

    @Query("SELECT * FROM favorite WHERE username=:username")
    suspend fun getUserByUsername(username: String): User

    @Delete
    suspend fun deleteByUsername(user: User)
}