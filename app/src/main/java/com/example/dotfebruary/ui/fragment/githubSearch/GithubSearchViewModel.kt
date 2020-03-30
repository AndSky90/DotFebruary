package com.example.dotfebruary.ui.fragment.githubSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState
import com.example.dotfebruary.network.NetworkProviderApi
import com.example.dotfebruary.repository.GithubRetrofitApi
import com.example.dotfebruary.repository.githubSearch.UsersPagedListRepository
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class GithubSearchViewModel : ViewModel(), KoinComponent {

    private val network by inject<NetworkProviderApi>()
    private val disposables = CompositeDisposable()

    private val repository = UsersPagedListRepository(
        network.getFeatureApiImpl(GithubRetrofitApi::class.java),
        disposables
    )

    private val queryString = MutableLiveData<String>()
    var searchText = ""
    var searchViewExpanded = true

    val userPagedList: LiveData<PagedList<GithubUser>> by lazy {
        Transformations.switchMap(queryString) { repository.fetchUsersListPage(it) }
    }

    val requestState: LiveData<RequestState> by lazy {
        Transformations.switchMap(userPagedList) { repository.getRequestStates() }
    }

    fun requestNewSearch(searchQuery: String) {
        queryString.postValue(searchQuery)
    }

    fun isListEmpty(): Boolean {
        return userPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}