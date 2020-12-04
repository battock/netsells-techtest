package com.firebase.netsells_techtest.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.data.LoadingState
import com.firebase.netsells_techtest.databinding.ActivityMainBinding
import com.firebase.netsells_techtest.model.RedditApiResponseChildren
import com.firebase.netsells_techtest.viewmodel.SubredditMainScreenViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val LOGGING_TAG = this.javaClass.simpleName

    private lateinit var viewModel: SubredditMainScreenViewModel
    private lateinit var listItems: List<RedditApiResponseChildren>
    private lateinit var listAdapter: HotSubmissionCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //instantiate the viewmodel
        viewModel = ViewModelProvider(this).get(SubredditMainScreenViewModel::class.java)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.viewModel = viewModel

        listItems = listOf()
        createList()


        setObservers()
        viewModel.fetchData()


    }

    //observes the live data in the viewmodel for any updates
    private fun setObservers() {
        viewModel.loadingState.observe(this, Observer<LoadingState> { loadingState ->
            when (loadingState) {
                LoadingState.LOADING -> {
                   Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                }
                LoadingState.ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
                LoadingState.SUCCESS -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                }
                else ->Toast.makeText(this, "", Toast.LENGTH_LONG).show()
            }
            Log.d(LOGGING_TAG, "loading state changed to $loadingState")
    })

        viewModel.apiDataList.observe(this, Observer<List<RedditApiResponseChildren>> { newList ->
            listAdapter.refreshList(newList)
        })


}



    private fun createList() {
      listAdapter = HotSubmissionCustomAdapter(this ,listOf())
     hotSubmissionsList.adapter = listAdapter
}
}
