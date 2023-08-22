package com.raisproject.githubuser.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.raisproject.githubuser.R
import com.raisproject.githubuser.adapter.SectionPagerAdapter
import com.raisproject.githubuser.data.Result
import com.raisproject.githubuser.databinding.ActivityDetailBinding
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.ui.ViewModelFactory
import com.raisproject.githubuser.utils.ViewStateCallback
import com.raisproject.githubuser.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), ViewStateCallback<User?> {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var username: String = ""


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = null
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = obtainViewModel(this@DetailActivity)

        getDetailUser()
        Log.d("TAG", "username-final: $username")
    }

    @SuppressLint("SuspiciousIndentation")
    private fun obtainViewModel(activity: DetailActivity): DetailViewModel {
    val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun getDetailUser() {
        val username = intent.getStringExtra("username").toString()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDetailUser(username).observe(this@DetailActivity) {
                when (it) {
                    is Result.Success -> onSuccess(it.data)
                    is Result.Loading -> onLoading()
                    is Result.Error -> onFailed(it.message)
                }
            }
        }
        showTabLayout(username)
    }

    private fun showTabLayout(username: String) {
        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabsLayout
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
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

    override fun onSuccess(data: User?) {
        binding.apply {
            binding.progressBar.visibility = View.INVISIBLE
            binding.tvMessage.visibility = View.INVISIBLE
            binding.cardComponent.visibility = View.VISIBLE
            Glide.with(application)
                .load(data?.avatar)
                .into(imageUser)
            title.text = data?.username
            tvName.text = data?.name
            tvLocation.text = data?.location
            tvFollowers.text = data?.followers.toString()
            tvFollowing.text = data?.following.toString()
            tvRepository.text = data?.repository.toString()

            if (data?.isFavorite == true) {
                fabLike.setImageResource(R.drawable.ic_favorite)
            } else {
                fabLike.setImageResource(R.drawable.ic_favorite_border)
            }

            fabLike.setOnClickListener {
                if (data?.isFavorite == true) {
                    data.isFavorite = false
                    viewModel.deleteFromFavorite(data)
                    fabLike.setImageResource(R.drawable.ic_favorite_border)
                    showToast(this@DetailActivity, "${data.username} is deleted from favorite")
                } else {
                    data?.isFavorite = true
                    data?.let { userData -> viewModel.insertToFavorite(userData) }
                    fabLike.setImageResource(R.drawable.ic_favorite)
                    showToast(this@DetailActivity, "${data?.username} is saved to favorite")
                }
            }
        }
    }

    override fun onLoading() {
        binding.apply {
            fabLike.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            tvMessage.visibility = View.VISIBLE
            binding.cardComponent.visibility = View.INVISIBLE
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            fabLike.visibility = View.INVISIBLE
            binding.cardComponent.visibility = View.INVISIBLE
            imageUser.visibility = View.INVISIBLE
            tvName.visibility = View.INVISIBLE
            tvLocation.visibility = View.INVISIBLE
            cardComponent.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            tvMessage.text = "Can't find detail" }
    }
}