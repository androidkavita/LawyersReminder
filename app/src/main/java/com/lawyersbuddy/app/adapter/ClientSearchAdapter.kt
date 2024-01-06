package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.fragments.clicklistener.Clientclick
import com.lawyersbuddy.app.databinding.ClientItemsBinding
import com.lawyersbuddy.app.model.HomeSearchData

class ClientSearchAdapter(val context : Context,
                          val list: List<HomeSearchData>,
                          var Clientclick: Clientclick) : RecyclerView.Adapter<ClientSearchAdapter.ViewHolder>() {
    private var listener: AdapterView.OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ClientItemsBinding = DataBindingUtil.bind(itemView)!!

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.client_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
            if (List!=null){
                holder.binding.tvName.text=List.name
                holder.binding.tvNumber.text=List.mobile

                holder.binding.llViewdetails.setOnClickListener {
                    Clientclick.Clientclick(List.id)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

