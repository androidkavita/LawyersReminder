package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowSearchResultBinding
import com.lawyersbuddy.app.model.HomeSearchData


class SearchAdapter (val context : Context, val list: List<HomeSearchData>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowSearchResultBinding = DataBindingUtil.bind(itemView)!!

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
            holder.binding.tvName.text = List.client_name
            holder.binding.tvCrnNo.text = List.cnr_number
            holder.binding.tvCaseTitle.text = List.case_title
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}