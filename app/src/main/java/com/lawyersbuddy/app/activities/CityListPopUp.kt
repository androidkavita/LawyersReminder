package com.lawyersbuddy.app.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.fragments.clicklistener.popupItemClickListnerCountry
import com.lawyersbuddy.app.model.CityListData

class CityListPopUp (var context: Context,
                     var data: ArrayList<CityListData>,
                     var flag: String,
                     var click: popupItemClickListnerCountry
) :
    RecyclerView.Adapter<CityListPopUp.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var ListData = data.get(position)


        if (flag == "City") {
            holder.Names.text = ListData.city
            holder.Names.setOnClickListener {
                ListData.city.let { it1 -> click.getCountry(it1, flag,0) }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Names: TextView


        init {
            Names = itemView.findViewById(R.id.content_textView)
        }
    }

    fun filterList(filteredList: ArrayList<CityListData>) {
        data = filteredList
        notifyDataSetChanged()

    }




}