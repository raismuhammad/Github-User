package com.raisproject.githubuser.ui.followers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisproject.githubuser.R
import com.raisproject.githubuser.adapter.FollowAdapter
import com.raisproject.githubuser.data.Result
import com.raisproject.githubuser.databinding.FragmentFollowersBinding
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.ui.ViewModelFactory
import com.raisproject.githubuser.utils.Constant
import com.raisproject.githubuser.utils.ViewStateCallback

class FollowersFragment : Fragment(), ViewStateCallback<List<User>> {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followAdapter: FollowAdapter
    private lateinit var viewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFollowersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        followAdapter = FollowAdapter()

        val type = arguments?.getString(Constant.BUNDLE_FOLLOW)
        val username = arguments?.getString(Constant.BUNDLE_USERNAME).toString()

        Log.d("TAG", "followers: $username")
        viewModel = obtainViewModel(this@FollowersFragment)
        showFollowers(username)
        showRecyclerView()

        return root
    }

    private fun showFollowers(username: String) {
        viewModel.getFollowers(username).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> onSuccess(it.data)
                is Result.Loading -> onLoading()
                is Result.Error -> onFailed(it.message)
            }
        }
    }

    private fun obtainViewModel(activity: FollowersFragment): FollowersViewModel {
        val factory = ViewModelFactory.getInstance(activity.requireActivity().application)
        return ViewModelProvider(activity, factory).get(FollowersViewModel::class.java)
    }

    private fun showRecyclerView() {
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.adapter = followAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSuccess(data: List<User>) {
        followAdapter.setFollowList(data)

        binding.apply {
            tvMessage.visibility = invisible
            progressBar.visibility = invisible
            rvFollowers.visibility = visible
        }
    }

    override fun onLoading() {
        binding.apply {
            progressBar.visibility = visible
            rvFollowers.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                tvMessage.text = resources.getString(R.string.not_following_anyone)
                tvMessage.visibility = visible
            } else {
                tvMessage.text = message
                tvMessage.visibility = visible
            }
            progressBar.visibility = invisible
            rvFollowers.visibility = invisible
        }
    }
}