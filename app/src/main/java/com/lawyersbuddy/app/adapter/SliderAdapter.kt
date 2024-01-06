package com.lawyersbuddy.app.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lawyersbuddy.app.CommonMethod.CommonMethods
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.model.BannerImg

import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(var context:Context, var sliderDataArrayList: ArrayList<BannerImg>) :
    RecyclerView.Adapter<SliderAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {

            var view: View? = LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, null)
            view?.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )!!
            return SliderAdapter.MyViewHolder(view!!)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: SliderAdapter.MyViewHolder, position: Int) {
            var Data = sliderDataArrayList[position]

            Log.d("TAG", "onBindViewHolder: "+Data.image)

            try {
//                Glide.with(context).load(Data.image).into(holder.image)
                Glide.with(context).load(Data.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.bg_contactus))
                    .apply(RequestOptions.errorOf(R.drawable.bg_contactus))
                    .into(holder.iv_auto_image_slider)



            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        override fun getItemCount(): Int {
         return sliderDataArrayList.size
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var iv_auto_image_slider = itemView.findViewById<ImageView>(R.id.iv_auto_image_slider)


        }
    }





//    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
//        val inflate: View =
//            LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, null)
//        return SliderAdapterViewHolder(inflate)
//    }
//
//    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
//        val sliderItem = sliderDataArrayList[position]
//
//        Glide.with(viewHolder.itemView)
//            .load(sliderItem.image)
//            .fitCenter()
//            .into(viewHolder.imageViewBackground)
//    }
//
//    override fun getCount(): Int {
//        return sliderDataArrayList.size
//    }
//
//    class SliderAdapterViewHolder(itemView: View) :
//        ViewHolder(itemView) {
//
//        var imageViewBackground: ImageView
//
//        init {
//            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
//
//        }
//    }

//}