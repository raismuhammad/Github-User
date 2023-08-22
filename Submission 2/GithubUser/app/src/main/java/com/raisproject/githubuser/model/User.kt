package com.raisproject.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite")
data class User(

    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("login")
    @ColumnInfo("username")
    val username: String = "",

    val name: String = "",

    @field:SerializedName("avatar_url")
    @ColumnInfo("avatar")
    val avatar: String = "",

    val url: String = "",

    val followers_url: String = "",

    val following_url: String = "",

    @field:SerializedName("public_repos")
    @ColumnInfo("repository")
    val repository: Int = 0,

    val followers: Int = 0,

    val following: Int = 0,

    val location: String = "",

    @ColumnInfo("isFavorite")
    var isFavorite: Boolean = false
) : Parcelable
