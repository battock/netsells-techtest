package com.firebase.netsells_techtest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.data.HotSubmissionsService
import com.firebase.netsells_techtest.data.LoadingState
import com.firebase.netsells_techtest.model.RedditApiResponseChildren
import com.firebase.netsells_techtest.model.HotSubApiResponse
import com.firebase.netsells_techtest.model.HotSubData
import retrofit2.Call
import retrofit2.Response


class SubredditMainScreenViewModel(private val hotSubmissionsService: HotSubmissionsService) :
    ViewModel() {

    //set from main activity based on res file
    var numberOfItemsToDisplay = 0
    private val LOGGING_TAG = this.javaClass.simpleName


    //the title of the main page
    //would ultimately come from a cms file but for sake of this task i am just hard coding here
    val hotlistHeader = "Reddit Hot Submissions"


    val loadingState = MutableLiveData<LoadingState>(LoadingState.NOT_LOADING)
    val apiDataList = MutableLiveData<List<HotSubData>>()
    var allApiItemsList: List<HotSubData>? = listOf()

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
                allApiItemsList = formatText(apiResponse?.body()?.hotSubmissionData?.children)
                addMoreListItems()
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


    //this method adds some more items to the list view depending on screen size
    //not all items from the api call are displayed on screen until user has scrolled
    //all the way down
    fun addMoreListItems() {
          apiDataList.value = allApiItemsList?.take(numberOfItemsToDisplay)
        if (numberOfItemsToDisplay < allApiItemsList?.size?:0) {
            val incrememntNumber = if(numberOfItemsToDisplay+ numberOfItemsToDisplay<allApiItemsList?.size?:0){numberOfItemsToDisplay}else{allApiItemsList?.size?:0}
            numberOfItemsToDisplay += incrememntNumber
        }
    }


}