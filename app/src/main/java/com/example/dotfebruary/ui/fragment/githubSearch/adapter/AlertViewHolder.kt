package com.example.dotfebruary.ui.fragment.githubSearch.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dotfebruary.R
import com.example.dotfebruary.model.RequestState
import kotlinx.android.synthetic.main.list_alert_view_holder.view.*

class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(requestState: RequestState) {
        if (requestState == RequestState.IN_PROGRESS) {
            itemView.progressBar.isVisible = true
            itemView.alertTitle.isVisible = false
        } else {
            itemView.progressBar.isVisible = false
            itemView.alertTitle.isVisible = true
            itemView.alertTitle.text = when (requestState) {
                RequestState.FAIL -> itemView.context.getString(R.string.error_loading_list)
                RequestState.LIST_COMPLETE -> itemView.context.getString(R.string.end_of_list)
                else -> ""
            }
        }
    }

}