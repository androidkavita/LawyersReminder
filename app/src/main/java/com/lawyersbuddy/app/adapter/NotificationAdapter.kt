package com.lawyersbuddy.app.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowNotificationItemBinding

import com.lawyersbuddy.app.model.NotificationData
import com.lawyersbuddy.app.util.DateFormat


class NotificationAdapter (val context : Context, var data: ArrayList<NotificationData>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {



    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: RowNotificationItemBinding = DataBindingUtil.bind(itemView)!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_notification_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = data[position]

        holder.binding.tvHead.text=List.title
        holder.binding.tvDetail.text=List.message
        holder.binding.tvDate.text = DateFormat.dateHearing(List.created_at)


    }

    override fun getItemCount(): Int {
        return data.size
    }


}