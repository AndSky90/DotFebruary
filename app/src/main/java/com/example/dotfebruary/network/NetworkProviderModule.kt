package com.example.dotfebruary.network

import org.koin.dsl.module

val networkProviderModule = module {
    single<NetworkProviderApi> { NetworkProviderImpl() }
}