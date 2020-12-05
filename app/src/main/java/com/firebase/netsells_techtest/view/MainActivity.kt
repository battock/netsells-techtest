package com.firebase.netsells_techtest.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
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
import com.firebase.netsells_techtest.viewmodel.SubRedditViewModelFactory
import com.firebase.netsells_techtest.viewmodel.SubredditMainScreenViewModel
import kotlinx.android.synthetic.main.activity_main.*

const val LIST_INCREMENT = 10

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
    //While api is loading we see a spinner
    private fun startLoadingSpinner() {
        Log.d(LOGGING_TAG, "api is losding so starting loading animation")
        progress_circular.visibility = View.VISIBLE
    }


    //once api has loaded or and error has occured the progress will stop and UI will be shown
    private fun stopLoadingSpinner() {
        Log.d(LOGGING_TAG, "loading animation stopping")
        progress_circular.visibility = View.INVISIBLE
    }

    //creates the list view and adds method to dynamically extend when scrolling
    private fun createList() {
        var currentFirstVisibleItem = 0
        var currentVisibleItemCount = 0
        var itemCount = 0
        var currentScrollState = 0
        var loadingMore = false
        var startIndex = 0L
        var offset = 10L

        listAdapter = HotSubmissionCustomAdapter(this, arrayListOf())
        hotSubmissionsList.adapter = listAdapter
        hotSubmissionsList.apply {


            setOnScrollListener(object:AbsListView.OnScrollListener{
                override fun onScroll( absListView:AbsListView, firstVisibleItem:Int, visibleItemCount:Int, totalItemCount:Int) {
                    currentFirstVisibleItem = firstVisibleItem;
                    currentVisibleItemCount = visibleItemCount;
                    itemCount = totalItemCount;
                }

                override fun onScrollStateChanged( absListView:AbsListView,  scrollState:Int) {
                    currentScrollState = scrollState;
                    isScrollCompleted(currentVisibleItemCount,currentScrollState,itemCount,currentFirstVisibleItem);
                }

            })

        }

    }

    //if user gets to end of first lot of list items some more will be added from the viewmodel
    private fun isScrollCompleted(currentVisibleItemCount:Int, currentScrollState:Int, totalItemCount:Int, currentFirstVisibleItem:Int) {
        if (currentVisibleItemCount > 0 && currentScrollState === SCROLL_STATE_IDLE && totalItemCount === currentFirstVisibleItem + currentVisibleItemCount) {
                Log.d(LOGGING_TAG, "user has scrolled to end of current list so adding more list items")
            if(viewModel.apiDataList.value?.size?:0<viewModel?.allApiItemsList?.size?:0) {
                viewModel.addMoreListItems()
            }
        }
    }

}
