package com.firebase.netsells_techtest.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.data.HotSubmissionsService
import com.firebase.netsells_techtest.data.LoadingState
import com.firebase.netsells_techtest.databinding.ActivityMainBinding
import com.firebase.netsells_techtest.model.HotSubData
import com.firebase.netsells_techtest.model.RedditApiResponseChildren
import com.firebase.netsells_techtest.viewmodel.SubredditMainScreenViewModel
import com.firebase.netsells_techtest.viewmodel.SubRedditViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val LOGGING_TAG = this.javaClass.simpleName

    private lateinit var viewModelFactory: SubRedditViewModelFactory
    private lateinit var viewModel: SubredditMainScreenViewModel
    private lateinit var listItems: List<RedditApiResponseChildren>
    private lateinit var listAdapter: HotSubmissionCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //instantiate the viewmodel
        viewModelFactory = SubRedditViewModelFactory(HotSubmissionsService())
        viewModel =ViewModelProvider(this, viewModelFactory)
            .get(SubredditMainScreenViewModel::class.java)

        //create data binding layout
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main);

        //assign viewmodel to data binding
        binding.viewModel = viewModel

        listItems = listOf()

        //instantiate the list view
        createList()

        //set up observers
        setObservers()

        //make intial api call on app opening
        viewModel.fetchData()


    }

    //observes the live data in the viewmodel for any updates
    private fun setObservers() {
        viewModel.loadingState.observe(this, Observer<LoadingState> { loadingState ->
            when (loadingState) {
                LoadingState.LOADING -> {
                    startLoadingSpinner()
                }
                LoadingState.ERROR -> {
                    stopLoadingSpinner()
                }
                LoadingState.SUCCESS -> {
                    stopLoadingSpinner()
                }
                else -> {
                }
            }
            Log.d(LOGGING_TAG, "loading state changed to $loadingState")
        })

        viewModel.apiDataList.observe(this, Observer<List<HotSubData>> { newList ->
            listAdapter.refreshList(newList)
        })


    }

    private fun startLoadingSpinner() {
        Log.d(LOGGING_TAG, "api is losding so starting loading animation")
        progress_circular.visibility = View.VISIBLE
    }


    private fun stopLoadingSpinner() {
        Log.d(LOGGING_TAG, "loading animation stopping")
        progress_circular.visibility = View.INVISIBLE
    }


    private fun createList() {
        listAdapter = HotSubmissionCustomAdapter(this, listOf())
        hotSubmissionsList.adapter = listAdapter
    }
}
