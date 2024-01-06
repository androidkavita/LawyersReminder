package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityAddNewCardBinding

class AddNewCardActivity : BaseActivity() {

    lateinit var binding: ActivityAddNewCardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_card)

        binding.arrow.setOnClickListener {
            finish()
        }
    }
}