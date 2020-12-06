package com.firebase.netsells_techtest.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import androidx.appcompat.app.AlertDialog
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


class MainActivity : AppCompatActivity() {

    private val LOGGING_TAG = this.javaClass.simpleName

    private lateinit var viewModelFactory: SubRedditViewModelFactory
    private lateinit var viewModel: SubredditMainScreenViewModel
    private lateinit var listItems: List<RedditApiResponseChildren>
    private lateinit var listAdapter: HotSubmissionCustomAdapter

    private var listIncrement = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listIncrement = resources.getInteger(R.integer.maxlistsize)


        //ensure screen does not rotate
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED)

        //instantiate the viewmodel
        viewModelFactory = SubRedditViewModelFactory(HotSubmissionsService())
        viewModel =ViewModelProvider(this, viewModelFactory)
            .get(SubredditMainScreenViewModel::class.java)


        //set list increment value
        viewModel.numberOfItemsToDisplay = listIncrement

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
                    showErrorDialog()
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

    //show error dialog
    //just a simple alert dialog to cover this part of the journey
    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Sorry, something went wrong")
            .setPositiveButton("Try again") {
                    dialog, tryAgainClick -> tryAgain()
            }
            .setNegativeButton( "quit app"){
                    dialog, tryAgainClick -> quitApp()
            }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }

    //forces th app to close
    private fun quitApp() {
        Log.d(LOGGING_TAG, "error, quit app selected, app is quitting")
        val i = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
        i.putExtra("android.intent.extra.KEY_CONFIRM", true)
        startActivity(i)
    }

    //retrys the api call
    private fun tryAgain(){
        Log.d(LOGGING_TAG, "error, try again triggerd")
        viewModel.fetchData()
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
