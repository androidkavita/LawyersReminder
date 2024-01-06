package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.HearingItemBinding
import com.lawyersbuddy.app.fragments.clicklistener.Click
import com.lawyersbuddy.app.model.HomeSearchData
import com.lawyersbuddy.app.util.DateFormat


class HearingSearchAdapter (val context : Context, val list: List<HomeSearchData>, var hearingCLick: Click) : RecyclerView.Adapter<HearingSearchAdapter.ViewHolder>() {
    private var listener: AdapterView.OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: HearingItemBinding = DataBindingUtil.bind(itemView)!!

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hearing_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
          holder.binding.tvHearingDate.text= DateFormat.NotificationDate(List.current_date)
          holder.binding.tvCnrnumber.text=List.cnr_number
          holder.binding.caseTitle.text=List.case_title
          holder.binding.nextHearingdate.text=DateFormat.NotificationDate(List.hearing_date)
            holder.binding.llViewdetails.setOnClickListener {
                hearingCLick.click(List.id.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

