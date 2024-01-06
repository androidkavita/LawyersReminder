package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityEditProfileBinding

class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)




    }
}