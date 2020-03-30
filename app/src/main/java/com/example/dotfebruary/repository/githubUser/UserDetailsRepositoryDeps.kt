package com.example.dotfebruary.repository.githubUser

import com.example.dotfebruary.network.NetworkProviderApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userDetailsRepositoryDependencies = module {
    single(named("userDetailsRepositoryModule")) {
        UserDetailsRepositoryDependencies("userDetailsRepositoryModule", get())
    }
}

class UserDetailsRepositoryDependencies (val name: String, val network: NetworkProviderApi)