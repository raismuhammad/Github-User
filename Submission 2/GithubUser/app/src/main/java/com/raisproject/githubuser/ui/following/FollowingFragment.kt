package com.raisproject.githubuser.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.raisproject.githubuser.R
import com.raisproject.githubuser.adapter.FollowAdapter
import com.raisproject.githubuser.data.Result
import com.raisproject.githubuser.databinding.FragmentFollowingBinding
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.ui.ViewModelFactory
import com.raisproject.githubuser.utils.Constant
import com.raisproject.githubuser.utils.ViewStateCallback

class FollowingFragment : Fragment(), ViewStateCallback<List<User>> {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followAdapter: FollowAdapter
    private lateinit var viewModel: FollowingViewModel
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentFollowingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        followAdapter = FollowAdapter()


        viewModel = obtainViewModel(this@FollowingFragment)

        val username = arguments?.getString(Constant.BUNDLE_USERNAME).toString()
//        viewModel.getFollowing(username)
        showFollowing(username)
        showRecyclerView()

        return root
    }

    private fun showFollowing(username: String) {
        viewModel.getFollowing(username).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> onSuccess(it.data)
                is Result.Loading -> onLoading()
                is Result.Error -> onFailed(it.message)
            }
        }
    }

    private fun obtainViewModel(activity: FollowingFragment): FollowingViewModel {
        val factory = ViewModelFactory.getInstance(activity.requireActivity().application)
        return ViewModelProvider(activity, factory).get(FollowingViewModel::class.java)
    }

    private fun showRecyclerView() {
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.adapter = followAdapter
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
            rvFollowing.visibility = visible
        }
    }

    override fun onLoading() {
        binding.apply {
            progressBar.visibility = visible
            rvFollowing.visibility = invisible
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
            rvFollowing.visibility = invisible
        }
    }
}