package com.raisproject.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import com.raisproject.githubuser.data.Repository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val repository = Repository(application)

    suspend fun getFavorite() = repository.getFavorite()
}