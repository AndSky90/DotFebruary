package com.example.dotfebruary.common

import android.view.View
import android.widget.SearchView
import android.widget.TextView
import com.example.dotfebruary.common.AppSettings.DEBOUNCE_SEARCH_MILLISECONDS
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun SearchView.getSearchObservable() =
    Observable
        .create(ObservableOnSubscribe<String> { subscriber ->
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    onActionViewCollapsed()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    subscriber.onNext(p0 ?: "")
                    return true
                }
            })
        })
        .distinct()
        .filter { it.isBlank().not() }
        .debounce(DEBOUNCE_SEARCH_MILLISECONDS, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())!!


fun TextView.setTextOrInvisible(string: String?) {
    if (!string.isNullOrBlank()) {
        this.visibility = View.VISIBLE
        this.text = string
    } else {
        this.visibility = View.GONE
    }
}

