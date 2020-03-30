package com.example.dotfebruary.ui.fragment.githubSearch.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.dotfebruary.R
import com.example.dotfebruary.databinding.AlertViewHolderBinding
import com.example.dotfebruary.model.RequestState

class AlertViewHolder(
    private val binding: AlertViewHolderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(requestState: RequestState) {
        binding.state = requestState
        binding.alertTitle.text = when (requestState) {
            RequestState.FAIL -> itemView.context.getString(R.string.error_loading_list)
            RequestState.LIST_COMPLETE -> itemView.context.getString(R.string.end_of_list)
            else -> ""
        }
        binding.executePendingBindings()
    }

}