package com.example.dotfebruary.repository.githubUser

import org.koin.dsl.module

val userDetailsRepositoryModule = module {
    factory<UserDetailsRepositoryApi> { UserDetailsRepository() }
}