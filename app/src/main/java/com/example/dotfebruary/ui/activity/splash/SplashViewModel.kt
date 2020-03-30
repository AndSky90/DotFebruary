package com.example.dotfebruary.ui.activity.splash

import androidx.lifecycle.ViewModel
import com.example.dotfebruary.model.FacebookProfile
import com.example.dotfebruary.repository.facebookProfile.FacebookRepositoryApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.inject
import org.koin.core.qualifier.named

class SplashViewModel : ViewModel(), KoinComponent {

    private val disposables = CompositeDisposable()
    private val di by inject<SplashActivityDependencies>(named("splashActivityModule"))
    private val repository: FacebookRepositoryApi

    init {
        loadKoinModules(splashActivityModule)
        repository = di.facebookRepository
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun saveFacebookProfileToDb(
        profile: FacebookProfile,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        disposables.add(Observable.fromCallable {
            repository.saveFacebookProfile(profile)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { onSuccess() },
                { onError() }
            )
        )
    }

}