package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityTermsandConditionsBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ContactUsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsandConditionsActivity : BaseActivity() {
    lateinit var binding: ActivityTermsandConditionsBinding
    private val Contactusviewmodel: ContactUsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_termsand_conditions)

        binding.arrow.setOnClickListener {
            finish()
        }

        Contactusviewmodel.errorString.observe(this) {
            toast(it)
        }
        Contactusviewmodel.term_conditionApi()
        Contactusviewmodel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        Contactusviewmodel.AboutUsResponse.observe(this) {
            if (it?.status == 1) {
                binding.description.text=it.data.description


            } else {
                //toast(it.message)
                snackbar(it?.message!!)
            }
        }

    }
}