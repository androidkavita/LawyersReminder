package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowHearingListItemBinding

import com.lawyersbuddy.app.fragments.clicklistener.Click
import com.lawyersbuddy.app.model.HearingData
import com.lawyersbuddy.app.util.DateFormat


class HearingAdapter (val context : Context, var data: ArrayList<HearingData>, var click: Click) :
    RecyclerView.Adapter<HearingAdapter.ViewHolder>() {

    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: RowHearingListItemBinding = DataBindingUtil.bind(itemView)!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_hearing_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val List = data[position]

        holder.binding.tvNextHearingDate.text= DateFormat.NotificationDate(List.currentDate)
        holder.binding.tvCaseTitle.text=List.caseTitle
        holder.binding.tvPartyName.text=List.clientName
        holder.binding.llViewdetails.setOnClickListener {
            click.click(List.id.toString())
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }


}