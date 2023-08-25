package com.raisproject.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisproject.githubuser.databinding.UserItemBinding
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.ui.detail.DetailActivity

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {

    var followList = ArrayList<User>()

    fun setFollowList(user: List<User>) {
        this.followList.clear()
        this.followList = user as ArrayList<User>
        notifyDataSetChanged()
    }

    inner class FollowViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.title.text = user.username.capitalize()
            binding.subTitle.visibility = View.GONE
            Glide.with(itemView.context)
                .load(user.avatar)
                .skipMemoryCache(true)
                .into(binding.imgUser)
            binding.lvItem.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("username", user.username)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        return FollowViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(followList[position])
    }
}