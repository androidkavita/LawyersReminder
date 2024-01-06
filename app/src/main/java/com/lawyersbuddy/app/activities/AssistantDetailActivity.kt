package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityAssistantDetailBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.AssistantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssistantDetailActivity : BaseActivity() {
    lateinit var binding: ActivityAssistantDetailBinding
    var client_id: String = ""
    private val viewModel: AssistantViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assistant_detail)

        if (intent != null) {
            client_id = intent.getIntExtra("id", 0).toString()
        }
        binding.arrow.setOnClickListener {
            finish()
        }
        viewModel.errorString.observe(this) {
            toast(it)
        }
        viewModel.assistantDetailApi(
            "Bearer " + userPref.getToken().toString(),
            client_id.toString()
        )

        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        viewModel.AddAssistantResponse.observe(this) {
            if (it?.status == 1) {
                try {
                    binding.clientname.text = it.data.name
                    binding.email.text = it.data.email
                    binding.phoneNumber.text = it.data.mobile
                    Glide.with(this).load(it.data?.image)
                        .placeholder(R.drawable.user_image_place_holder).into(binding.ivPict)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                snackbar(it?.message!!)
            }
        }
    }

}