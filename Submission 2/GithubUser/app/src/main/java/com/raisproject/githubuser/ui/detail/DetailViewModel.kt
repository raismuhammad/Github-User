package com.raisproject.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raisproject.githubuser.data.Repository
import com.raisproject.githubuser.model.User
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : ViewModel() {
    private val repository: Repository = Repository(application)

    fun insertToFavorite(user: User) = viewModelScope.launch {
        repository.insertToFavorite(user)
    }

    fun deleteFromFavorite(user: User) = viewModelScope.launch {
        repository.deleteFromFavorite(user)
    }

    suspend fun getDetailUser(username: String) = repository.getDetailUser(username)

}