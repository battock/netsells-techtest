package com.firebase.netsells_techtest.data

import com.firebase.netsells_techtest.model.HotSubApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface HotSubmissionsApi {
    @GET("Android/hot.json")
    fun getHotSubmissionsList(): Call<HotSubApiResponse>
}