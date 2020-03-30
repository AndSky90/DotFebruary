package com.example.dotfebruary.repository.githubSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.dotfebruary.common.AppSettings.USERS_PAGINATION_SIZE
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState
import com.example.dotfebruary.repository.GithubRetrofitApi
import io.reactivex.disposables.CompositeDisposable

class UsersPagedListRepository(
    private val api: GithubRetrofitApi,
    private val disposables: CompositeDisposable
) : UsersPagedListRepositoryApi {
    private lateinit var userPagedList: LiveData<PagedList<GithubUser>>
    private var userDataSourceFactory: UserDataSourceFactory =
        UserDataSourceFactory("", api, disposables)

    override fun fetchUsersListPage(
        searchQuery: String
    ): LiveData<PagedList<GithubUser>> {
        userDataSourceFactory =
            UserDataSourceFactory(searchQuery, api, disposables)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(USERS_PAGINATION_SIZE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
        return userPagedList
    }

    override fun getRequestStates(): LiveData<RequestState> {
        return Transformations.switchMap<UserDataSource, RequestState>(
            userDataSourceFactory.userLiveDataSource, UserDataSource::requestState
        )
    }
}