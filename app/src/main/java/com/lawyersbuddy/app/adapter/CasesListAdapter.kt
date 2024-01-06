package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.fragments.clicklistener.Clickcase
import com.lawyersbuddy.app.fragments.clicklistener.caseedit
import com.lawyersbuddy.app.databinding.RowCaseListItemBinding
import com.lawyersbuddy.app.model.CasesData
import com.lawyersbuddy.app.prefs.UserPref
import com.lawyersbuddy.app.util.DateFormat

class CasesListAdapter(
    val context: Context,
    var list:ArrayList<CasesData>,
    var userPref: UserPref,
    var click: Clickcase, var case: caseedit
) : RecyclerView.Adapter<CasesListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowCaseListItemBinding = DataBindingUtil.bind(itemView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasesListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_case_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CasesListAdapter.ViewHolder, position: Int) {
        val List = list[position]

        if (userPref.getType().equals("1")) {
            holder.binding.ivEdit.visibility = View.VISIBLE
        } else {
            if (userPref.getPermission().equals("0")) {
                holder.binding.ivEdit.visibility = View.GONE
            } else {
                holder.binding.ivEdit.visibility = View.VISIBLE

            }
        }

        holder.binding.casesTitle.text = List.case_title
        holder.binding.caseStage.text = List.case_stage
        holder.binding.cnrNumber.text = List.cnr_number
        holder.binding.clientName.text = List.client_name
        holder.binding.date.text =
          DateFormat.date(List.start_date)
        holder.binding.llViewdetails.setOnClickListener {
            click.click(List.id)
        }    
        holder.binding.ivEdit.setOnClickListener {
           case.clickedit(List.id)
        }
        holder.binding.date.text =
         DateFormat.date(List.start_date)

    }


    override fun getItemCount(): Int {
        return list.size
    }
}


/* (val context : Context, var data: ArrayList<CasesData>, var click: Click) :
    RecyclerView.Adapter<CasesListAdapter.ViewHolder>() {
//    private var listener: OnItemClickListener? = null

    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: RowCaseListItemBinding = DataBindingUtil.bind(itemView)!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_case_list_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = data[position]

        holder.binding.casesTitle.text=List.case_title
        holder.binding.courtName.text=List.court_name
        holder.binding.judgeName.text=List.judge_name
        holder.binding.partyName.text=List.opponent_party_name
        holder.binding.llViewdetails.setOnClickListener {
            click.click(List.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}*/