package com.raisproject.githubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisproject.githubuser.R
import com.raisproject.githubuser.adapter.UserAdapter
import com.raisproject.githubuser.databinding.ActivityMainBinding
import com.raisproject.githubuser.ui.favorite.FavoriteActivity
import com.raisproject.githubuser.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var searchAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchAdapter = UserAdapter()

        observeProgressbar()
        searchUser()
        showViewModel()
        showRecyclerView()

        binding.title.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_fav -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.btn_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }

    private fun searchUser() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    searchView.clearFocus()
                    viewModel.search(searchView.text.toString())
                    binding.ivNotFound.visibility = View.GONE
                    true
                }

        }
    }

    private fun showRecyclerView() {
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = searchAdapter
    }

    private fun showViewModel() {
        viewModel.searchList.observe(this) {
            showLoading(true)
            if (it.size != 0) {
                binding.ivNotFound.visibility = View.GONE
                showLoading(false)
                binding.rvUser.visibility = View.VISIBLE
                searchAdapter.setUserList(it)
            } else {
                binding.ivNotFound.visibility = View.VISIBLE
                showLoading(false)
                binding.rvUser.visibility = View.GONE
                Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeProgressbar() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        viewModel.isLoading.observe(this) {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }
}