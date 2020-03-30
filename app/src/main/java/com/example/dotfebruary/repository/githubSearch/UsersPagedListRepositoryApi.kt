package com.example.dotfebruary.repository.githubSearch

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState

interface UsersPagedListRepositoryApi {

    fun fetchUsersListPage(searchQuery: String): LiveData<PagedList<GithubUser>>

    fun getRequestStates(): LiveData<RequestState>
}