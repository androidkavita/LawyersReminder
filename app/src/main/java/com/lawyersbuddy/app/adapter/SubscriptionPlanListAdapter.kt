package com.lawyersbuddy.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowSubscriptionPlanBinding
import com.lawyersbuddy.app.model.SubscriptionplanData

class SubscriptionPlanListAdapter(val list: List<SubscriptionplanData>, val listener: OnClick) :
    RecyclerView.Adapter<SubscriptionPlanListAdapter.ViewHolder>() {

    var selectedPosition = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowSubscriptionPlanBinding = DataBindingUtil.bind(itemView)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_subscription_plan, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = list[position]

        holder.binding.tvAmount.text = "₹${data.amount.toString()}"
        holder.binding.tvPlanType.text = data.days.toString()
//        holder.binding.tvMonths.text = data.p


        holder.binding.rlSubscription.setOnClickListener {

            listener.onSubscriptionPlanClicked(data)
            selectedPosition = position
            notifyDataSetChanged()

        }

        if (position == selectedPosition) {
            holder.binding.ivSelect.visibility = View.VISIBLE
            holder.binding.rlSubscription.setBackgroundResource(R.drawable.bg_select_subscription)
        } else {
            holder.binding.rlSubscription.setBackgroundResource(R.drawable.bg_white_grey_border_rounded)
            holder.binding.ivSelect.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClick {
        fun onSubscriptionPlanClicked(subscriptionModelData: SubscriptionplanData) {

        }
    }
}/*SubscriptionPlanListAdapter(val context: Context, val list: List<String>) :
    RecyclerView.Adapter<SubscriptionPlanListAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowSubscriptionPlanListBinding = DataBindingUtil.bind(itemView)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_subscription_plan_list, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = list[position]


    }

    override fun getItemCount(): Int {

        return list.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }


}*/