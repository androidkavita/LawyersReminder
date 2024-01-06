package com.lawyersbuddy.app.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.adapter.MyPaymentsAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityMyPaymentsBinding
import com.lawyersbuddy.app.model.PaymentListData
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MyPaymentsActivity : BaseActivity() {
    private val viewModel: PaymentViewModel by viewModels()

    private lateinit var binding: ActivityMyPaymentsBinding
    private var list:ArrayList<PaymentListData> = ArrayList()
    lateinit var adapter: MyPaymentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_payments)

        viewModel.PaymentListApi("Bearer "+userPref.getToken().toString())

        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
       binding.arrow.setOnClickListener {
           finish()
       }
        viewModel.errorString.observe(this) {
            toast(it)
        }
        viewModel.PaymentListResponse.observe(this) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)
                binding.rvMypayments.visibility= View.VISIBLE
                binding.tvNoEvents.visibility=View.GONE

                binding.rvMypayments.layoutManager = LinearLayoutManager(this)
                adapter = MyPaymentsAdapter(this,list)

                binding.rvMypayments.adapter =adapter
                adapter.notifyDataSetChanged()

            } else {
                binding.rvMypayments.visibility= View.GONE
                binding.tvNoEvents.visibility=View.VISIBLE

            }
        }


    }
}