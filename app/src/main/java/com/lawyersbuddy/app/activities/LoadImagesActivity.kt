package com.lawyersbuddy.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.adapter.CasesListAdapter
import com.lawyersbuddy.app.adapter.HearingImagesAdapter
import com.lawyersbuddy.app.fragments.clicklistener.Clickcase
import com.lawyersbuddy.app.databinding.ActivityLoadImagesBinding
import com.lawyersbuddy.app.model.ImagesModel

class LoadImagesActivity : AppCompatActivity() , Clickcase {
    lateinit var binding:ActivityLoadImagesBinding
    lateinit var addimage:ArrayList<String>
    lateinit var addimage1:ArrayList<ImagesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_load_images)
        if (intent!=null){
            addimage=intent.getStringArrayListExtra("addimage")!!
        }


    }

    override fun click(id: Int) {
    }
}