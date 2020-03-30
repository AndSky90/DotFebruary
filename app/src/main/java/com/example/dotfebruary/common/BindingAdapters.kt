package com.example.dotfebruary.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.dotfebruary.R
import com.squareup.picasso.Picasso

@BindingAdapter("app:setTextOrInvisible")
fun setTextOrInvisible(view: TextView, string: String?) {
    if (!string.isNullOrBlank()) {
        view.visibility = View.VISIBLE
        view.text = string
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:setAvatarWithPicasso")
fun setAvatarWithPicasso(view: ImageView?, url: String?) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.ic_person_grey_24dp)
        .error(R.drawable.ic_person_grey_24dp)
        .fit()
        .centerCrop()
        .into(view)

}