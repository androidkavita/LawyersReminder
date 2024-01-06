package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityClientItemDetailBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientItemDetailActivity : BaseActivity() {
    lateinit var binding: ActivityClientItemDetailBinding
    var client_id :String= ""
    private val ClientItemViewModel: ClientViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_item_detail)

        if (intent != null) {
            client_id = intent.getIntExtra("id",0).toString()
        }
        binding.arrow.setOnClickListener {
            finish()
        }
        ClientItemViewModel.client_detail("Bearer " + userPref.getToken().toString(), client_id.toString())

        ClientItemViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        ClientItemViewModel.errorString.observe(this) {
            toast(it)
        }
        ClientItemViewModel.ClientDetailsResponse.observe(this) {
            if (it?.status == 1) {
                try {
                    binding.clientname.text = it.data.name
                    binding.address.text = it.data.address
                    binding.email.text = it.data.email
                    binding.phoneNumber.text = it.data.mobile
                    Glide.with(this).load(it.data.image)
                        .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                        .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                        .into(binding.ivPict)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                snackbar(it?.message!!)
            }
        }
    }
}