package com.raisproject.githubuser.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raisproject.githubuser.databinding.FavItemBinding
import com.raisproject.githubuser.model.User
import com.raisproject.githubuser.ui.detail.DetailActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var favoriteList = ArrayList<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setFavoriteList(user: List<User>) {
        this.favoriteList.clear()
        this.favoriteList = user as ArrayList<User>
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(val binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind(favorite : User) {
            binding.apply {
                tvUsername.text = favorite.username
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .skipMemoryCache(true)
                    .into(imgUser)
                cardItem.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("username", favorite.username)
                    itemView.context.startActivity(intent)
                }
            }
        }
//        init {
//            binding.root.setOnClickListener {
//                onClick.invoke(favoriteList[adapterPosition])
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(FavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }
}