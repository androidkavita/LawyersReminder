package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.activities.StatePopup
import com.lawyersbuddy.app.fragments.clicklistener.popupItemClickListnerCountry
import com.lawyersbuddy.app.model.ClientListData
import com.lawyersbuddy.app.model.SearchResponsedataData
import com.lawyersbuddy.app.model.StateListData

class SearchListAdapter (
    var context: Context,
    var data: ArrayList<ClientListData>,
    var flag: String,
    var click: popupItemClickListnerCountry
) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListAdapter.ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return SearchListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListAdapter.ViewHolder, position: Int) {
        var ListData = data.get(position)


        if (flag == "Clients") {
            holder.Names.text = ListData.name
            holder.Names.setOnClickListener {
                ListData.name.let { it1 -> click.getCountry(it1!!, flag, ListData.id!!.toInt()) }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    fun filterList(filteredStateList: ArrayList<ClientListData>) {
        data = filteredStateList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Names: TextView


        init {
            Names = itemView.findViewById(R.id.content_textView)
        }
    }

    interface ClickListener {
        fun onClick(position: Int, id: Int, title: String)
    }
}

