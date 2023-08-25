package com.raisproject.githubuser.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisproject.githubuser.adapter.FavoriteAdapter
import com.raisproject.githubuser.data.Result
import com.raisproject.githubuser.databinding.ActivityFavoriteBinding
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.ui.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = null
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = obtainViewModel(this@FavoriteActivity)
        favoriteAdapter = FavoriteAdapter()

        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = favoriteAdapter

        getFavorite()
    }

    private fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.VISIBLE
    }

    private fun onSuccess(data: List<User>) {
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvMessage.visibility = View.INVISIBLE
        favoriteAdapter.setFavoriteList(data)
    }

    private fun getFavorite() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavorite().observe(this@FavoriteActivity) {
                when (it) {
                    is Result.Success -> onSuccess(it.data)
                    is Result.Loading -> onLoading()
                    is Result.Error -> onFailed(it.message)
                }
            }
        }
    }

    private fun onFailed(message: String?) {
        if (message == null) {
            binding.rvFavorite.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.tvMessage.visibility = View.VISIBLE
            binding.tvMessage.text = "Favorite is empty"
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavorite().observe(this@FavoriteActivity) {
                when (it) {
                    is Result.Success -> onSuccess(it.data)
                    is Result.Loading -> onLoading()
                    is Result.Error -> onFailed(it.message)
                }
            }
        }
    }

    private fun obtainViewModel(activity: FavoriteActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}