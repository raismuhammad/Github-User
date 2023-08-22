package com.raisproject.githubuser.data.remote.retrofit

import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun search(
        @Query("q") username: String
    ) : Call<UserResponse>

    @GET("/users/{username}")
    fun detailUser(
        @Path("username") username: String
    ) : Call<User>

    @GET("/users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username : String
    ) : Call<List<User>>
}