package com.example.dotfebruary.ui.activity.main

import com.example.dotfebruary.repository.facebookProfile.FacebookRepositoryApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainActivityModule = module {
    single(named("mainActivityModule")) {
        MainActivityDependencies("mainActivityModule", get())
    }
}

class MainActivityDependencies(
    val name: String,
    val facebookRepository: FacebookRepositoryApi
)