package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowMypaymentsItemBinding
import com.lawyersbuddy.app.model.PaymentListData


class MyPaymentsAdapter (val context : Context, val list: List<PaymentListData>) : RecyclerView.Adapter<MyPaymentsAdapter.ViewHolder>() {
        private var listener: AdapterView.OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowMypaymentsItemBinding = DataBindingUtil.bind(itemView)!!

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_mypayments_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
            holder.binding.subscriptionName.text = List.subscription_name
            holder.binding.price.text = "-${List.amount.toString()}"
            holder.binding.paymentBy.text = List.payment_by
            holder.binding.date.text = List.payment_date
            holder.binding.time.text = List.payment_time
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}