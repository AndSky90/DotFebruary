package com.example.dotfebruary.repository

import com.example.dotfebruary.common.AppSettings
import com.example.dotfebruary.model.GithubSearchResult
import com.example.dotfebruary.model.GithubUserDetails
import io.reactivex.Observable
import retrofit2.http.*


interface GithubRetrofitApi {

    @GET("/search/users")
    fun getGithubUsers(
        @Query("q") queryString: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = AppSettings.USERS_PAGINATION_SIZE
    ) : Observable<GithubSearchResult>

    @GET("/users/{userName}")
    fun getUserDetails(
        @Path("userName") userName: String
    ) : Observable<GithubUserDetails>

}