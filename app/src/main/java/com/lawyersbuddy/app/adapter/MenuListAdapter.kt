package com.lawyersbuddy.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.RowMenuItemBinding
import com.lawyersbuddy.app.model.DashboardMenuModel

class MenuListAdapter(private val context: Context,
                      private val dataSource: ArrayList<DashboardMenuModel>) : BaseAdapter() {

    override
    fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent:
    ViewGroup
    ): View? {

        var roeMenuItemBinding: RowMenuItemBinding? = null

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (roeMenuItemBinding == null) {
            roeMenuItemBinding =
                DataBindingUtil.inflate(inflater, R.layout.row_menu_item, parent, false)
        }

        roeMenuItemBinding!!.tvHeaderName.text = dataSource[position].title
        Glide.with(context).load( dataSource[position].icon).into(roeMenuItemBinding!!.ivIcon)


        return roeMenuItemBinding.root
    }

}