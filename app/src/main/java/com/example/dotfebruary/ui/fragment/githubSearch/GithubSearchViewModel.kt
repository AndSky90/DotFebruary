package com.example.dotfebruary.ui.fragment.githubSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.dotfebruary.common.NetworkProviderImpl
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState
import com.example.dotfebruary.repository.GithubRetrofitApi
import com.example.dotfebruary.repository.githubSearch.UsersPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class GithubSearchViewModel : ViewModel() {

    private val repository = UsersPagedListRepository(
        NetworkProviderImpl().getFeatureApiImpl(
            GithubRetrofitApi::class.java
        )
    )

    private val disposables = CompositeDisposable()
    private val queryString = MutableLiveData<String>()
    val userPagedList: LiveData<PagedList<GithubUser>>

    val requestState: LiveData<RequestState> by lazy {
        repository.getRequestStates()
    }

    init {
        queryString.value = "chris"
        userPagedList = Transformations.switchMap(queryString) {
            repository.fetchUsersListPage(it, disposables)
        }
    }

    fun requestNewSearch(searchQuery: String) {
        queryString.postValue(searchQuery)
    }

    fun isListEmpty(): Boolean = userPagedList.value?.isEmpty() ?: true

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}