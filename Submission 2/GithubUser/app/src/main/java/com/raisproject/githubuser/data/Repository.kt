package com.raisproject.githubuser.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raisproject.githubuser.data.datastore.SettingDataStore
import com.raisproject.githubuser.data.local.dao.FavoriteDao
import com.raisproject.githubuser.data.local.database.UserDatabase
import com.raisproject.githubuser.data.remote.retrofit.ApiConfig
import com.raisproject.githubuser.data.remote.retrofit.ApiService
import com.raisproject.githubuser.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(application: Application) {

    private val dao: FavoriteDao
    private val retrofit: ApiService
    private val dataStore: SettingDataStore

    init {
        val db =  UserDatabase.getDatabase(application)
        dao = db.getFavoriteDao()
        retrofit = ApiConfig.getApiService()
        dataStore = SettingDataStore.getInstance(application)
    }

    suspend fun getFavorite() : LiveData<Result<List<User>>> {

        val listFavorite = MutableLiveData<Result<List<User>>>()

        if (dao.getAllFavorite().isEmpty()) {
            listFavorite.postValue(Result.Error(null))
        } else {
            listFavorite.postValue(Result.Success(dao.getAllFavorite()))
        }
        return listFavorite
    }

    suspend fun insertToFavorite(user: User) {
        dao.insert(user)
    }

    suspend fun deleteFromFavorite(user: User) {
        dao.deleteByUsername(user)
    }

    suspend fun getDetailUser(username: String) : LiveData<Result<User>> {

        val user = MutableLiveData<Result<User>>()

        if (dao.getUserByUsername(username) != null) {
            user.postValue(Result.Success(dao.getUserByUsername(username)))
        } else {
            retrofit.detailUser(username).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val result = response.body()
                    if (result == null) {
                        Log.d("TAG", "onResponse: $response")
                    } else {
                        user.postValue(Result.Success(result))
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("TAG", "onFailure: ${t.message}")
                }

            })
        }
        return user
    }

    fun getFollowing(username: String) : LiveData<Result<List<User>>> {

        val listFollowing = MutableLiveData<Result<List<User>>>()

        listFollowing.postValue(Result.Loading)
        retrofit.getFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val result = response.body()
                if (result!!.isEmpty()) {
                    listFollowing.postValue(Result.Error(null))
                } else {
                    listFollowing.postValue(Result.Success(result))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}")
            }

        })
        return listFollowing
    }

    fun getFollowers(username: String) : LiveData<Result<List<User>>> {

        val listFollowers = MutableLiveData<Result<List<User>>>()

        listFollowers.postValue(Result.Loading)
        retrofit.getFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val result = response.body()
                if (result!!.isEmpty()) {
                    listFollowers.postValue(Result.Error(null))
                } else {
                    listFollowers.postValue(Result.Success(result))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        return listFollowers
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = dataStore.saveThemeSettings(isDarkModeActive)

    fun getThemeSetting() = dataStore.getThemeSetting()
}