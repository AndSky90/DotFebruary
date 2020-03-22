package com.example.dotfebruary

import android.app.Application
import android.util.Log
import io.reactivex.plugins.RxJavaPlugins

@Suppress("UNUSED")
class App : Application() {

    /**защита от переполнения стека saveInstanceState*/
    override fun onCreate() {
        super.onCreate()

       /* startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@com.example.dotfebruary.App)
            modules(
                listOf(
                    navigatorModule,
                    networkModule,
                    userProviderModule,
                    loaderModule
                )
            )
        }*/

        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RxJavaError","$throwable") }
    }

}