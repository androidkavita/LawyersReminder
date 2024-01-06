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
import com.lawyersbuddy.app.databinding.RowMyoffersBinding
import com.lawyersbuddy.app.model.OffersData


class OffersAdapter (val context : Context, var data: ArrayList<OffersData>) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {



    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: RowMyoffersBinding = DataBindingUtil.bind(itemView)!!
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_myoffers, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = data[position]

        holder.binding.tvDiscount.text= List.discount.toString()
        holder.binding.tvCouponCode.text=List.code
        holder.binding.tvValiddate.text = "Valid till: "+List.validated


    }

    override fun getItemCount(): Int {
        return data.size
    }


}