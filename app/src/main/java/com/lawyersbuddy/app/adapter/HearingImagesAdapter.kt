package com.lawyersbuddy.app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.ImageBinding

import com.lawyersbuddy.app.fragments.clicklistener.imageclick
import com.lawyersbuddy.app.model.ImagesModel
import java.util.ArrayList

class HearingImagesAdapter(val context: Context, var data: ArrayList<String>, var click: imageclick) :
    RecyclerView.Adapter<HearingImagesAdapter.ViewHolder>() {

    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: ImageBinding = DataBindingUtil.bind(itemView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            Glide.with(context).load(data[position]).placeholder(R.drawable.ic_baseline_picture_as_pdf_24).into(holder.binding.image)
            Log.d("TAG", "onBindViewHolder: "+data[position])

            holder.binding.cross.setOnClickListener {
                click.imageclick(position)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
    override fun getItemCount(): Int {
        return data.size
    }


}