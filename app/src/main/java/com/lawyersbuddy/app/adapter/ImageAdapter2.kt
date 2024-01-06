package com.lawyersbuddy.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.ImageItemBinding
import com.lawyersbuddy.app.databinding.PreviousImageBinding
import com.lawyersbuddy.app.fragments.clicklistener.ClickIMage
import com.lawyersbuddy.app.fragments.clicklistener.DeleteImage
import com.lawyersbuddy.app.fragments.clicklistener.clickpdf
import com.lawyersbuddy.app.model.file_detail

class ImageAdapter2  (val context : Context, val list: List<file_detail>, var clickimage: ClickIMage,var deleteImage: DeleteImage) : RecyclerView.Adapter<ImageAdapter2.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: PreviousImageBinding = DataBindingUtil.bind(itemView)!!

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.previous_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val List = list[position]
        try {
            Glide.with(context).load(List.path_name).placeholder(R.drawable.ic_baseline_picture_as_pdf_24).into(holder.binding.image)

//            if ()
            holder.binding.image.setOnClickListener {
                if (List.file_type==0){

                    clickimage.click(List.path_name)
                }
            }
            holder.binding.cross.setOnClickListener {
                deleteImage.deleteimage(List.file_name)
            }



        } catch (e: Exception) {
            e.printStackTrace()
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }
}

