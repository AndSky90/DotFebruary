package com.example.dotfebruary.model

import com.google.gson.annotations.SerializedName

data class GithubSearchResult (

    @SerializedName("total_count")
    var totalCount : Int,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,

    @SerializedName("items")
    var items: List<GithubUser>
)