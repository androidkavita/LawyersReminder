package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityContactusBinding
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.CommonUtils.containsAnyOfIgnoreCase
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ContactUsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Contactus : BaseActivity() {
    private lateinit var binding: ActivityContactusBinding
    private val Contactusviewmodel: ContactUsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contactus)

        binding.arrow.setOnClickListener {
            finish()
        }
//        Contactusviewmodel.ContactUsApi()
        Contactusviewmodel.errorString.observe(this) {
            toast(it)
        }
        Contactusviewmodel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        Contactusviewmodel.ContactUsResponse.observe(this) {
            if (it?.status == 1) {
                snackbar(it?.message!!)
                supportFragmentManager.let { ContactUsDialog().show(it,"MyCustomFragment") }

                binding.message.setText("")
            } else {
                snackbar(it?.message!!)
            }
        }
        binding.btnUpdate.setOnClickListener {
          if (binding.message.text.toString().trim().isEmpty()) {
                 toast("Enter message.")
             }
            else if (binding.message.text.toString().length<10){
                toast("Enter valid message.")
          }
            else{
                 Contactusviewmodel.add_contact(binding.message.text.toString(),userPref.getName().toString(),userPref.getEmail().toString())

             }
        }


    }
}