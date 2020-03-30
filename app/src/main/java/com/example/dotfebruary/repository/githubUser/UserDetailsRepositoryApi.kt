package com.example.dotfebruary.repository.githubUser

import com.example.dotfebruary.model.GithubUserDetails
import io.reactivex.Observable

interface UserDetailsRepositoryApi {
    fun requestUserDetails(
        userName: String
    ): Observable<GithubUserDetails>

    fun recycle()
}