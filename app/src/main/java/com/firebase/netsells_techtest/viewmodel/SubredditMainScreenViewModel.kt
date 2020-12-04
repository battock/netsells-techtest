package com.firebase.netsells_techtest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.netsells_techtest.data.HotSubmissionsService
import com.firebase.netsells_techtest.data.LoadingState
import com.firebase.netsells_techtest.model.RedditApiResponseChildren
import com.firebase.netsells_techtest.model.HotSubApiResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class SubredditMainScreenViewModel : ViewModel() {

    private val LOGGING_TAG = this.javaClass.simpleName

    //the title of the main page
    //would ultimately come from a cms file but for sake of this task i am just hard coding here
    val hotlistHeader ="Reddit Hot Submissions"

    private val hotSubmissionsService=HotSubmissionsService()
    private val disposable=CompositeDisposable()

    val loadingState =MutableLiveData<LoadingState>(LoadingState.NOT_LOADING)
    var apiDataList=MutableLiveData<List<RedditApiResponseChildren>>()

    //retrieves data and updates the UI
    fun fetchData() {
        loadingState.value = LoadingState.LOADING
        disposable.add(
            hotSubmissionsService.getHotSubmissionsList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<HotSubApiResponse>(){
                    override fun onSuccess(apiResponse: HotSubApiResponse) {
                        loadingState.value = LoadingState.SUCCESS
                        apiDataList.value = apiResponse?.hotSubmissionData.children
                        Log.d(LOGGING_TAG,"api success with ${apiResponse?.hotSubmissionData.children}") }

                    override fun onError(e: Throwable) {
                        loadingState.value = LoadingState.ERROR
                        Log.d(LOGGING_TAG,"api success with ${e.message}}")

                    }

                })

        )

    }



}