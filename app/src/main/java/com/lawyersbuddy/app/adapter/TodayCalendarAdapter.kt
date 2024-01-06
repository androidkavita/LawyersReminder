package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowTodaycalendarListItemBinding

import com.lawyersbuddy.app.fragments.clicklistener.Click
import com.lawyersbuddy.app.model.CalenderListData
import com.lawyersbuddy.app.util.DateFormat


class TodayCalendarAdapter(
    val context: Context,
    val list: List<CalenderListData>,
    var click: Click
) : RecyclerView.Adapter<TodayCalendarAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowTodaycalendarListItemBinding = DataBindingUtil.bind(itemView)!!

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_todaycalendar_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
            holder.binding.llViewDetails.setOnClickListener {
                click.click(List.id.toString())
            }
            holder.binding.hearingDate.text = DateFormat.NotificationDate(List.current_date)
            holder.binding.serialno.text = "S.No. ${List.s_no}"
            holder.binding.clientName.text = List.client_name
            holder.binding.casetitle.text = List.case_title
            holder.binding.cnr.text = List.case_cnr_number
            holder.binding.stage.text = List.case_stage.toString()


           } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}