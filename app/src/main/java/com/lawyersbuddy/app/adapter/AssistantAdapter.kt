package com.lawyersbuddy.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowAssistantListItemBinding
import com.lawyersbuddy.app.model.AssistantListData


class AssistantAdapter (val context : Context, var data: ArrayList<AssistantListData>) :
    RecyclerView.Adapter<AssistantAdapter.ViewHolder>() {
//    private var listener: OnItemClickListener? = null

    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: RowAssistantListItemBinding = DataBindingUtil.bind(itemView)!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssistantAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_assistant_list_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AssistantAdapter.ViewHolder, position: Int) {
        val List = data[position]

        holder.binding.name.text=List.name
        holder.binding.emailId.text=List.email
        holder.binding.phoneNumber.text=List.mobile


    }

    override fun getItemCount(): Int {
        return data.size
    }


}