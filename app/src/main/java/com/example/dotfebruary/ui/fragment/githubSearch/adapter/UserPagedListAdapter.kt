package com.example.dotfebruary.ui.fragment.githubSearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dotfebruary.R
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_alert_view_holder.view.*
import kotlinx.android.synthetic.main.user_view_holder.view.*

class UserPagedListAdapter(
    private val onClick: (String) -> Unit
) : PagedListAdapter<GithubUser, RecyclerView.ViewHolder>(UserDiffCallback()) {

    companion object {
        const val USER_VIEW_TYPE = 1
        const val ALERT_VIEW_TYPE = 2
    }

    private var requestState = RequestState.SUCCESS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == USER_VIEW_TYPE) {
            val view = layoutInflater.inflate(R.layout.user_view_holder, parent, false)
            UserItemViewHolder(view, onClick)
        } else {
            val view = layoutInflater.inflate(R.layout.list_alert_view_holder, parent, false)
            AlertViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == USER_VIEW_TYPE) {
            (holder as UserItemViewHolder).bind(getItem(position))
        } else {
            (holder as AlertViewHolder).bind(requestState)
        }
    }


    private fun hasExtraRow(): Boolean =
        requestState != RequestState.SUCCESS


    override fun getItemCount(): Int =
        super.getItemCount() + if (hasExtraRow()) 1 else 0

    override fun getItemViewType(position: Int): Int =
        if (hasExtraRow() && position == itemCount - 1) {
            ALERT_VIEW_TYPE
        } else {
            USER_VIEW_TYPE
        }

    fun setNetworkState(state: RequestState) {
        val previousState = this.requestState
        val hadExtraRow = hasExtraRow()
        this.requestState = state
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != state) {
            notifyItemChanged(itemCount - 1)
        }
    }


    class UserDiffCallback : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser) =
            oldItem == newItem
    }
}