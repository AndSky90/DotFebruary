package com.example.dotfebruary.repository.githubSearch

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.repository.GithubRetrofitApi
import io.reactivex.disposables.CompositeDisposable

class UserDataSourceFactory(
    private val searchQuery: String,
    private val api: GithubRetrofitApi,
    private val disposables: CompositeDisposable
) : DataSource.Factory<Int, GithubUser>() {

    val userLiveDataSource = MutableLiveData<UserDataSource>()

    override fun create(): DataSource<Int, GithubUser> {
        val userDataSource =
            UserDataSource(
                searchQuery,
                api,
                disposables
            )
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }
}