package com.lawyersbuddy.app.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.adapter.NotificationAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityNotificationBinding
import com.lawyersbuddy.app.model.NotificationData
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NotificationActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val notificationViewModel: NotificationViewModel by viewModels()

    lateinit var adapter: NotificationAdapter
    private var list:ArrayList<NotificationData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)

        notificationViewModel.errorString.observe(this) {
            toast(it)
        }
        binding.arrow.setOnClickListener {
            finish()
        }

        notificationViewModel.notification_list("Bearer "+userPref.getToken().toString())

        notificationViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        notificationViewModel.notificationListResponse.observe(this) {
            if (it?.status == 1) {
                binding.rvNotification.visibility= View.VISIBLE
                binding.tvNoEvents.visibility= View.GONE
                list.clear()
                list.addAll(it.data)
                adapter = NotificationAdapter(this,list)
                binding.rvNotification.adapter =adapter
                  adapter.notifyDataSetChanged()

            } else {
                binding.rvNotification.visibility= View.GONE
                binding.tvNoEvents.visibility=View.VISIBLE
            }
        }


    }
}