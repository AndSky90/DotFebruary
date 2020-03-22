package com.example.dotfebruary.common

import com.example.dotfebruary.BuildConfig.LOGGING_ENABLED_BY_GRADLE
import com.example.dotfebruary.common.AppSettings.BASE_URL
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkProviderImpl : NetworkProviderApi {

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        @Suppress("ConstantConditionIf")
        level = if (LOGGING_ENABLED_BY_GRADLE) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient: OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(5, TimeUnit.SECONDS)
            .callTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofitImpl: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    override fun getOkHttp(): OkHttpClient = okHttpClient

    override fun getRetrofit(): Retrofit = retrofitImpl

    override fun <T> getFeatureApiImpl(tApi: Class<T>): T = retrofitImpl.create(tApi)

}