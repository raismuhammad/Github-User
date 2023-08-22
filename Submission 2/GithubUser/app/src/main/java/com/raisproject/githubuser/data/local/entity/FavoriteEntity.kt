package com.raisproject.githubuser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class FavoriteEntity (
    var username: String = "",
    var avatarUi: String? = null
)