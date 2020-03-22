package com.example.dotfebruary.repository

import com.example.dotfebruary.common.NetworkProviderImpl
import com.example.dotfebruary.model.GithubSearchResult
import com.example.dotfebruary.model.GithubUserDetails
import io.reactivex.Observable


class UserDetailsRepository {

    private val githubApi = NetworkProviderImpl().getFeatureApiImpl(GithubRetrofitApi::class.java)

    fun requestUserDetails(
        userName: String
    ): Observable<GithubUserDetails> =
        githubApi.getUserDetails(userName)

}