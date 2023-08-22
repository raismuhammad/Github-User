package com.raisproject.githubuser.ui.followers

import android.app.Application
import androidx.lifecycle.ViewModel
import com.raisproject.githubuser.data.Repository

class FollowersViewModel(application: Application) : ViewModel() {

    val repository: Repository = Repository(application)

    fun getFollowers(username: String) = repository.getFollowers(username)
}