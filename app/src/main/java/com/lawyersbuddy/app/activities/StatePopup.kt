package com.lawyersbuddy.app.activities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.fragments.clicklistener.popupItemClickListnerCountry
import com.lawyersbuddy.app.model.StateListData


class StatePopup (

    var context: Context,
    var data: ArrayList<StateListData>,
    var flag: String,
    var click: popupItemClickListnerCountry
    ) :
    RecyclerView.Adapter<StatePopup.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val mInflater = LayoutInflater.from(context)
            val view = mInflater.inflate(R.layout.lists, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var ListData = data.get(position)


            if (flag == "State") {
                holder.Names.text = ListData.state
                holder.Names.setOnClickListener {
                    ListData.state.let { it1 -> click.getCountry(it1, flag, ListData.id.toInt()) }
                }
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun filterList(filteredStateList: ArrayList<StateListData>) {
            data = filteredStateList
            notifyDataSetChanged()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var Names: TextView


            init {
                Names = itemView.findViewById(R.id.content_textView)
            }
        }

}