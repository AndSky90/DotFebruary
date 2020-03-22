package com.example.dotfebruary.common

import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface NetworkProviderApi {
    fun getOkHttp(): OkHttpClient
    fun getRetrofit(): Retrofit
    fun <T> getFeatureApiImpl(tApi: Class<T>): T
}