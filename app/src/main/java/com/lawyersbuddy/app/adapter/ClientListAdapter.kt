package com.lawyersbuddy.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.BR


import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseModel
import com.lawyersbuddy.app.databinding.RowClientListItemBinding
import com.lawyersbuddy.app.prefs.UserPref
import com.lawyersbuddy.app.util.OnItemClickListener

class ClientListAdapter<T : BaseModel> internal constructor(
    val clientList: List<BaseModel>, var userPref: UserPref, val onItemClickListener: OnItemClickListener<T>,
) : RecyclerView.Adapter<ClientListAdapter.AgentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder =
        AgentViewHolder.from(parent)


    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.setVariable(BR.itemClickListener, onItemClickListener)

        if (userPref.getType().equals("1")) {
            holder.binding.ivEdit.visibility = View.VISIBLE
        } else {
            if (userPref.getPermission().equals("0")) {
                holder.binding.ivEdit.visibility = View.GONE
            } else {
                holder.binding.ivEdit.visibility = View.VISIBLE

            }
        }

    }

    private fun getItem(position: Int): BaseModel {
        return clientList[position]
    }

    override fun getItemCount(): Int = clientList.size

    class AgentViewHolder(val binding: RowClientListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentProduct: BaseModel) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AgentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: RowClientListItemBinding = DataBindingUtil
                    .inflate(
                        layoutInflater, R.layout.row_client_list_item,
                        parent, false
                    )
                return AgentViewHolder(binding)
            }
        }
    }
}
/*(val context : Context, var data: ArrayList<ClientListData>,var click:Click) :
    RecyclerView.Adapter<ClientListAdapter.ViewHolder>() {
//    private var listener: OnItemClickListener? = null

    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding:  RowClientListItemBinding = DataBindingUtil.bind(itemView)!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_client_list_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = data[position]

       holder.binding.clientname.text=List.name
        holder.binding.hearingDate.text=List.mobile
        holder.binding.llViewdetails.setOnClickListener {
            click.click(List.id.toString())
        }
        holder.binding.ivEdit.setOnClickListener {
            click.click(List.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}*/