package com.firebase.netsells_techtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firebase.netsells_techtest.data.HotSubmissionsService

class SubRedditViewModelFactory (private val hotSubsService:HotSubmissionsService = HotSubmissionsService()) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // If model class is correct return them as ViewModel with Value
        if(modelClass.isAssignableFrom(SubredditMainScreenViewModel::class.java)){
            return  SubredditMainScreenViewModel(hotSubsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}