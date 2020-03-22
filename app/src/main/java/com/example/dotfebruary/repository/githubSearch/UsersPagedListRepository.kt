package com.example.dotfebruary.repository.githubSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.dotfebruary.common.AppSettings.USERS_PAGINATION_SIZE
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState
import com.example.dotfebruary.repository.GithubRetrofitApi
import com.example.dotfebruary.repository.UserDataSource
import com.example.dotfebruary.repository.UserDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class UsersPagedListRepository(private val api: GithubRetrofitApi) {

    lateinit var userPagedList: LiveData<PagedList<GithubUser>>
    lateinit var userDataSourceFactory: UserDataSourceFactory

    fun fetchUsersListPage(
        searchQuery: String,
        disposables: CompositeDisposable
    ): LiveData<PagedList<GithubUser>> {
        userDataSourceFactory = UserDataSourceFactory(searchQuery, api, disposables)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(USERS_PAGINATION_SIZE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
        return userPagedList
    }

    fun getRequestStates(): LiveData<RequestState> {
        return Transformations.switchMap<UserDataSource, RequestState>(
            userDataSourceFactory.userLiveDataSource, UserDataSource::requestState
        )
    }
}