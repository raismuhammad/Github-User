package com.raisproject.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.model.UserResponse
import com.raisproject.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _searchList = MutableLiveData<ArrayList<User>>()
    val searchList: LiveData<ArrayList<User>> = _searchList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun search(username: String) {
        try {
            _isLoading.value = true
            val response = ApiConfig.getApiService().search(username)
            response.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _searchList.value = ArrayList(responseBody.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
        } catch (e: Exception) {
            Log.d("TAG", "search: ${e.message}")
        }
    }
}