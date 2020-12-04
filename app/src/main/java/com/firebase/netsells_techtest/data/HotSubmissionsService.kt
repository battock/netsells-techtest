package com.firebase.netsells_techtest.data

import com.firebase.netsells_techtest.model.HotSubApiResponse
import com.firebase.netsells_techtest.model.HotSubmission
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class HotSubmissionsService {
    private val BASE_URL = "https://www.reddit.com/r/"
    private val api: HotSubmissionsApi

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

       api = retrofit.create(HotSubmissionsApi::class.java)
    }

    fun getHotSubmissionsList(): Single<HotSubApiResponse> {
        return api.getHotSubmissionsList()
    }

}