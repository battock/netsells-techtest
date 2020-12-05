package com.firebase.netsells_techtest.model

import com.google.gson.annotations.SerializedName

data class HotSubmission (
    @SerializedName("modhash") val modhash : String?,
    @SerializedName("dist") val dist : Int,
    @SerializedName("children") val children : List<RedditApiResponseChildren>?,
    @SerializedName("after") val after : String?,
    @SerializedName("before") val before : String?
)

data class HotSubApiResponse(
    @SerializedName("kind") val kind : String?,
    @SerializedName("data") val hotSubmissionData : HotSubmission?
)


data class RedditApiResponseChildren (
    @SerializedName("kind") val kind : String?,
    @SerializedName("data") val data : HotSubData?

)

data class HotSubData (
    @SerializedName("author_fullname") var author : String?,
    @SerializedName("title") val title : String?,
    @SerializedName("url") val url : String? = ""

)
