package com.example.dotfebruary.repository.facebookProfile.facebookDatabase

import org.koin.dsl.module

val facebookDatabaseModule = module {
    single<FacebookDatabaseApi> { FacebookProfileRoomDb.getDatabase(get()) }
}