package com.firebase.netsells_techtest.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.startActivity
import com.firebase.netsells_techtest.databinding.HotSubmissionListItemBinding
import com.firebase.netsells_techtest.model.HotSubData


class HotSubmissionCustomAdapter(context: Context, var items: List<HotSubData>) :
    ArrayAdapter<HotSubData>(context, 0,items) {

    private val LOGGING_TAG: String? = this.javaClass.simpleName
    init {

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = HotSubmissionListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.hotSubmissionItem = getItem(position)

        (binding.root).setOnClickListener{
            openWebLink(getItem(position)?.url)
        }
        return binding.root
    }


    private fun openWebLink(url:String?) {
        url.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url ?: "")
            startActivity(context, intent, null)
        }
    }

    fun refreshList(newList: List<HotSubData>) {
        Log.d(LOGGING_TAG, "main list updating with $newList")
        this.items = newList
        notifyDataSetChanged()
    }


    override fun getCount(): Int {
        return items.count()
    }
    override fun getItem(position:Int): HotSubData? {
        return items?.get(position)
    }


}