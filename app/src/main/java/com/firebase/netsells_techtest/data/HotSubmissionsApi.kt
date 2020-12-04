package com.firebase.netsells_techtest.data

import com.firebase.netsells_techtest.model.HotSubmission
import io.reactivex.Single
import retrofit2.http.GET

interface HotSubmissionsApi {
    @GET("")
    fun getHotSubmissionsList(): Single<List<HotSubmission>>
}