package com.example.dotfebruary.repository.facebookProfile

import org.koin.dsl.module

val facebookRepositoryModule = module {
    single<FacebookRepositoryApi> { FacebookRepository() }
}