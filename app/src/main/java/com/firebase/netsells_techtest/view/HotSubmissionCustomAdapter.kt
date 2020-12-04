package com.firebase.netsells_techtest.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListAdapter
import com.firebase.netsells_techtest.databinding.HotSubmissionListItemBinding
import com.firebase.netsells_techtest.model.HotSubmission

class HotSubmissionCustomAdapter(context: Context, items: List<HotSubmission>) :
    ArrayAdapter<HotSubmission>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = HotSubmissionListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.hotSubmissionItem = getItem(position)

        return binding.root
    }
}