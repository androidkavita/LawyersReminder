package com.lawyersbuddy.app.activities

import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityAboutusBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ContactUsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Aboutus : BaseActivity() {
    lateinit var binding: ActivityAboutusBinding
    private val Contactusviewmodel: ContactUsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_aboutus)

        Contactusviewmodel.errorString.observe(this) {
            toast(it)
        }
        binding.arrow.setOnClickListener {
            finish()
        }
        Contactusviewmodel.aboutusApi()
        Contactusviewmodel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        Contactusviewmodel.AboutUsResponse.observe(this) {
            if (it?.status == 1) {
                binding.description.text=Html.fromHtml(it.data.description)


            } else {
                //toast(it.message)
                snackbar(it?.message!!)
            }
        }
    }
}