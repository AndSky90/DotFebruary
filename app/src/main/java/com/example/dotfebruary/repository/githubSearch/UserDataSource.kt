package com.example.dotfebruary.repository.githubSearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.dotfebruary.common.AppSettings
import com.example.dotfebruary.common.AppSettings.USERS_FIRST_PAGE
import com.example.dotfebruary.common.AppSettings.USERS_PAGINATION_SIZE
import com.example.dotfebruary.model.GithubUser
import com.example.dotfebruary.model.RequestState
import com.example.dotfebruary.repository.GithubRetrofitApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class UserDataSource(
    private val searchQuery: String,
    private val api: GithubRetrofitApi,
    private val disposables: CompositeDisposable
) : PageKeyedDataSource<Int, GithubUser>() {

    val requestState = MutableLiveData<RequestState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GithubUser>
    ) {
        requestState.postValue(RequestState.IN_PROGRESS)
        disposables.add(
            api.getGithubUsers(searchQuery, USERS_FIRST_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.totalCount <= USERS_PAGINATION_SIZE)
                            requestState.value = RequestState.LIST_COMPLETE
                        else
                            callback.onResult(it.items, null, USERS_FIRST_PAGE + 1)
                    }, {
                        handleThrowable(it, "loadInitial")
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GithubUser>) {
        requestState.postValue(RequestState.IN_PROGRESS)
        disposables.add(
            api.getGithubUsers(searchQuery, params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.totalCount / USERS_PAGINATION_SIZE + 1 >= params.key) {
                            callback.onResult(it.items, params.key + 1)
                            requestState.value = RequestState.SUCCESS
                        } else {
                            requestState.value = RequestState.LIST_COMPLETE
                        }
                    }, {
                        handleThrowable(it, "loadAfter")
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GithubUser>) {
    }

    private fun handleThrowable(ex: Throwable, sourceTag: String) {
        if (ex is HttpException && ex.code() == 403) {
            requestState.value = RequestState.FORBIDDEN
            Log.e(AppSettings.LOG_TAG, "$sourceTag ${ex.message} ")
        }
        requestState.value = RequestState.FAIL
        Log.e(AppSettings.LOG_TAG, "$sourceTag ${ex.message} ")
    }

}