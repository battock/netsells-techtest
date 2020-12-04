package com.firebase.netsells_techtest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.netsells_techtest.model.HotSubmission

class SubredditMainScreenViewModel : ViewModel() {


    //the title of the main page
    val hotlistHeader ="hot submission title"

    //retrieves data and updates the UI
    fun fetchData() {

    }

    //temporary list to test list view
    fun getListItems(): List<HotSubmission> {
        return listOf(
            HotSubmission(title="test title", author = "test author"),HotSubmission(title="test title", author = "test author"),HotSubmission(title="test title", author = "test author")
        )
    }

}