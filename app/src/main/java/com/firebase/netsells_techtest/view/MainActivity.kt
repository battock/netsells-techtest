package com.firebase.netsells_techtest.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.databinding.ActivityMainBinding
import com.firebase.netsells_techtest.model.HotSubmission
import com.firebase.netsells_techtest.viewmodel.SubredditMainScreenViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: SubredditMainScreenViewModel
    lateinit var listItems:List<HotSubmission>
    lateinit var listAdapter:HotSubmissionCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //instantiate the viewmodel
        viewModel = ViewModelProvider(this).get(SubredditMainScreenViewModel::class.java)
        val binding:ActivityMainBinding=DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.viewModel = viewModel

        listItems = viewModel.getListItems()
        createList()

        viewModel.fetchData()


    }

    @SuppressLint("ResourceType")
    private fun createList() {
        listAdapter = HotSubmissionCustomAdapter(this,listItems)
        hotSubmissionsList.adapter = listAdapter
    }
}
