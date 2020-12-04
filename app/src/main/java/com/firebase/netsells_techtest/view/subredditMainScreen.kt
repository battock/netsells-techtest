package com.firebase.netsells_techtest.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.viewmodel.SubredditMainScreenViewModel

class subredditMainScreen : Fragment() {

    companion object {
        fun newInstance() = subredditMainScreen()
    }

    private lateinit var viewModel: SubredditMainScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subreddit_main_screen_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubredditMainScreenViewModel::class.java)
    }

}