package com.raisproject.githubuser.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raisproject.githubuser.data.local.dao.FavoriteDao
import com.raisproject.githubuser.data.local.entity.FavoriteEntity
import com.raisproject.githubuser.model.User

@Database(
    entities = [User::class],
    version = 3
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getFavoriteDao() : FavoriteDao

    companion object {
        private var INSTANCE : UserDatabase? = null

        fun getDatabase(context: Context) : UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    UserDatabase::class.java,
                    "favorite_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}