package com.firebase.netsells_techtest.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.firebase.netsells_techtest.databinding.HotSubmissionListItemBinding
import com.firebase.netsells_techtest.model.HotSubData
import com.firebase.netsells_techtest.model.RedditApiResponseChildren

class HotSubmissionCustomAdapter(context: Context, var items: List<RedditApiResponseChildren>) :
    ArrayAdapter<RedditApiResponseChildren>(context, 0,items) {

    private val LOGGING_TAG: String? = this.javaClass.simpleName
    init {

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = HotSubmissionListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.hotSubmissionItem = getItem(position)?.data

        return binding.root
    }

    fun refreshList(newList: List<RedditApiResponseChildren>) {
        Log.d(LOGGING_TAG, "main list updating with $newList")
        this.items = newList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items.count()
    }
    override fun getItem(position:Int): RedditApiResponseChildren? {
        return items?.get(position)
    }
}