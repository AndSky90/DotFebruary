package com.example.dotfebruary.ui.activity.splash

import com.example.dotfebruary.repository.facebookProfile.FacebookRepositoryApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val splashActivityModule = module {
    single(named("splashActivityModule")) {
        SplashActivityDependencies("splashActivityModule", get())
    }
}

class SplashActivityDependencies(
    val name: String,
    val facebookRepository: FacebookRepositoryApi
)