package com.example.dotfebruary.ui.fragment.githubUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotfebruary.common.AppSettings.LOG_TAG
import com.example.dotfebruary.model.GithubUserDetails
import com.example.dotfebruary.model.RequestState
import com.example.dotfebruary.repository.githubUser.UserDetailsRepositoryApi
import com.example.dotfebruary.repository.githubUser.userDetailsRepositoryModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.inject

class GithubUserViewModel : ViewModel(), KoinComponent {

    private val repository by inject<UserDetailsRepositoryApi>()

    init {
        loadKoinModules(userDetailsRepositoryModule)
    }

    private val disposables = CompositeDisposable()
    val user = MutableLiveData<GithubUserDetails>()
    val requestState = MutableLiveData<RequestState>()

    fun loadUserDetails(userName: String) {
        if (userName.isBlank()) {
            requestState.value = RequestState.FAIL
        } else {
            requestState.value = RequestState.IN_PROGRESS
            disposables.add(
                repository.requestUserDetails(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            requestState.value = RequestState.SUCCESS
                            user.value = it
                        }, {
                            requestState.value = RequestState.FAIL
                            Log.e(LOG_TAG, "requestPageUsers ${it.message} ")
                        }
                    )
            )
        }
    }

    override fun onCleared() {
        disposables.clear()
        repository.recycle()
        unloadKoinModules(userDetailsRepositoryModule)
        super.onCleared()
    }

}