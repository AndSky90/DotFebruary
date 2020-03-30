package com.example.dotfebruary.repository.githubUser

import com.example.dotfebruary.model.GithubUserDetails
import com.example.dotfebruary.repository.GithubRetrofitApi
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.inject
import org.koin.core.qualifier.named


class UserDetailsRepository : UserDetailsRepositoryApi, KoinComponent {

    private val di by inject<UserDetailsRepositoryDependencies>(named("userDetailsRepositoryModule"))

    private val githubApi: GithubRetrofitApi

    init {
        loadKoinModules(userDetailsRepositoryDependencies)
        githubApi = di.network.getFeatureApiImpl(GithubRetrofitApi::class.java)
    }

    override fun requestUserDetails(
        userName: String
    ): Observable<GithubUserDetails> =
        githubApi.getUserDetails(userName)

    override fun recycle() {
        unloadKoinModules(userDetailsRepositoryDependencies)
    }
}