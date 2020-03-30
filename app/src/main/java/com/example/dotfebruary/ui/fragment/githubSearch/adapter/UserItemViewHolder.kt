package com.example.dotfebruary.ui.fragment.githubSearch.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.dotfebruary.databinding.UserViewHolderBinding
import com.example.dotfebruary.model.GithubUser

class UserItemViewHolder(
    private val binding: UserViewHolderBinding,
    val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: GithubUser) {
        binding.user = user
        binding.executePendingBindings()
        itemView.setOnClickListener { onClick(user.login) }
    }

}