package com.example.dotfebruary

import android.app.Application
import android.util.Log
import com.example.dotfebruary.network.networkProviderModule
import com.example.dotfebruary.repository.facebookProfile.facebookDatabase.facebookDatabaseModule
import com.example.dotfebruary.repository.facebookProfile.facebookRepositoryModule
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("UNUSED")
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    networkProviderModule,
                    facebookDatabaseModule,
                    facebookRepositoryModule
                )
            )
        }

        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RxJavaError","$throwable") }
    }

}