package com.raisproject.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raisproject.githubuser.ui.followers.FollowersFragment
import com.raisproject.githubuser.ui.following.FollowingFragment
import com.raisproject.githubuser.utils.Constant

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        val followingFragment = FollowingFragment()
        val followingBundle = Bundle()
        followingBundle.putString(Constant.BUNDLE_FOLLOW, Constant.TYPE_FOLLOWING)
        followingBundle.putString(Constant.BUNDLE_USERNAME, username)
        followingFragment.arguments = followingBundle

        val followersFragment = FollowersFragment()
        val followersBundle = Bundle()
        followersBundle.putString(Constant.BUNDLE_FOLLOW, Constant.TYPE_FOLLOWERS)
        followersBundle.putString(Constant.BUNDLE_USERNAME, username)
        followersFragment.arguments = followersBundle

        return when (position) {
            0 -> followingFragment
            else -> followersFragment
        }
    }

}