package com.example.dotfebruary.ui.fragment.githubSearch.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dotfebruary.R
import com.example.dotfebruary.model.GithubUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_view_holder.view.*

class UserItemViewHolder(view: View, val onClick: (String) -> Unit) :
    RecyclerView.ViewHolder(view) {

    fun bind(user: GithubUser?) {
        itemView.userLogin.text = user?.login
        itemView.adminMark.isVisible = user?.siteAdmin ?: false
        Picasso.get()
            .load(user?.avatarUrl)
            .placeholder(R.drawable.ic_person_grey_24dp)
            .error(R.drawable.ic_person_grey_24dp)
            .fit()
            .centerCrop()
            .into(itemView.userImage)

        itemView.setOnClickListener {
            if (user?.login.isNullOrBlank().not())
                onClick(user!!.login)
        }
    }

}