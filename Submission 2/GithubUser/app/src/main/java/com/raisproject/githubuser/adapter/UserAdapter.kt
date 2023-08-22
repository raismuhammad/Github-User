package com.raisproject.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.databinding.UserItemBinding
import com.raisproject.githubuser.ui.detail.DetailActivity

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var userList = ArrayList<User>()

    fun setUserList(user: List<User>) {
        this.userList.clear()
        this.userList = user as ArrayList<User>
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(itemView) {
                binding.title.text = user.username
                binding.subTitle.text = user.location
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .skipMemoryCache(true)
                    .into(binding.imgUser)
                binding.lvItem.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("username", user.username)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }
}