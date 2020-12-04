package com.firebase.netsells_techtest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.viewmodel.SubredditMainScreenViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: SubredditMainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //instantiate the viewmodel
        viewModel = ViewModelProvider(this).get(SubredditMainScreenViewModel::class.java)

        //viewModel.updateView()
    }
}
