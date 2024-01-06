package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.CaseItemsBinding
import com.lawyersbuddy.app.fragments.clicklistener.Clickcase
import com.lawyersbuddy.app.model.HomeSearchData

class CasesSearchAdapter (val context : Context, val list: List<HomeSearchData>, var clickcase: Clickcase) : RecyclerView.Adapter<CasesSearchAdapter.ViewHolder>() {
    private var listener: AdapterView.OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: CaseItemsBinding = DataBindingUtil.bind(itemView)!!

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.case_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
         holder.binding.casesTitle.text=List.case_title
//         holder.binding.isCaseClosed.text=List.cas
         holder.binding.clientname.text=List.client_name
         holder.binding.tvCnrnumber.text=List.cnr_number

            holder.binding.click.setOnClickListener {
                clickcase.click(List.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

