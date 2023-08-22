package com.raisproject.githubuser.ui.following

import android.app.Application
import androidx.lifecycle.ViewModel
import com.raisproject.githubuser.data.Repository

class FollowingViewModel(application: Application) : ViewModel() {

    val repository: Repository = Repository(application)

    fun getFollowing(username: String) = repository.getFollowing(username)


}