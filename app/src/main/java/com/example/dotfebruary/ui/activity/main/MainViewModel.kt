package com.example.dotfebruary.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dotfebruary.model.FacebookProfile
import com.example.dotfebruary.repository.facebookProfile.FacebookRepositoryApi
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.inject
import org.koin.core.qualifier.named

class MainViewModel : ViewModel(), KoinComponent {

    private val disposables = CompositeDisposable()
    private val di by inject<MainActivityDependencies>(named("mainActivityModule"))
    private val repository : FacebookRepositoryApi

    init {
        loadKoinModules(mainActivityModule)
        repository = di.facebookRepository
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun getFacebookProfileFromDb() : LiveData<FacebookProfile> {
       return repository.getFacebookProfile()
    }

}