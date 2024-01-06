package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityCheckoutBinding

class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout)


        binding.llAddCard.setOnClickListener {
            startActivity(Intent(this, AddNewCardActivity::class.java))
        }

        binding.llProceed.setOnClickListener {
            startActivity(Intent(this, CongratulationActivity::class.java))
        }

    }
}