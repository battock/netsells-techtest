package com.firebase.netsells_techtest.data

import com.firebase.netsells_techtest.model.HotSubApiResponse
import retrofit2.Call
import retrofit2.Response

interface DataServiceCallBack {
    fun onFail()
    fun onSucceed()
}