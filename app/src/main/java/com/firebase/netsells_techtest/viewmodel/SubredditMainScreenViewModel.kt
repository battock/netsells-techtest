package com.firebase.netsells_techtest.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.netsells_techtest.data.HotSubmissionsApi
import com.firebase.netsells_techtest.data.HotSubmissionsService
import com.firebase.netsells_techtest.data.LoadingState
import com.firebase.netsells_techtest.model.RedditApiResponseChildren
import com.firebase.netsells_techtest.model.HotSubApiResponse
import com.firebase.netsells_techtest.model.HotSubData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback


class SubredditMainScreenViewModel(private val hotSubmissionsService: HotSubmissionsService) :
    ViewModel() {

    private val LOGGING_TAG = this.javaClass.simpleName

    //the title of the main page
    //would ultimately come from a cms file but for sake of this task i am just hard coding here
    val hotlistHeader = "Reddit Hot Submissions"


    val loadingState = MutableLiveData<LoadingState>(LoadingState.NOT_LOADING)
    val apiDataList = MutableLiveData<List<HotSubData>>()


    //retrieves data and updates the UI
    fun fetchData() {

        loadingState.value = LoadingState.LOADING
        val call = hotSubmissionsService.getHotSubmissionsList()

        call.enqueue(object : retrofit2.Callback<HotSubApiResponse> {

            override fun onFailure(call: Call<HotSubApiResponse>, t: Throwable) {
                loadingState.value = LoadingState.ERROR
                Log.d(LOGGING_TAG, "api success with ${t.message}}")
            }

            override fun onResponse(call: Call<HotSubApiResponse>,apiResponse: Response<HotSubApiResponse>)
            {
                loadingState.value = LoadingState.SUCCESS
                apiDataList.value = formatText(apiResponse?.body()?.hotSubmissionData?.children)
                Log.d(
                    LOGGING_TAG,
                    "api success with ${apiResponse?.body()?.hotSubmissionData?.children}"
                )
            }

        })
    }

    //adds written by prefix to authro field and coverts list to HotSubData
    fun formatText(listToEdit: List<RedditApiResponseChildren?>?): List<HotSubData>? {
        val returnList = arrayListOf<HotSubData>()
        listToEdit?.forEach { it ->
            it?.let {
                returnList?.add(
                    HotSubData(
                        author = "Post by ${it?.data?.author ?: ""}",
                        title = it?.data?.title,
                        url = it?.data?.url
                    )
                )
            }
        }
        return returnList
    }


}