package com.lawyersbuddy.app.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivitySettingBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.NotificationViewModel
import com.lawyersbuddy.app.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class SettingActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingBinding
    private val SettingViewModel: NotificationViewModel by viewModels()
    lateinit var type:String
    var view=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        binding.ivArrow.setOnClickListener(this)

        if (intent!=null){
            view=intent.getStringExtra("view").toString()
        }

        SettingViewModel.notification_status("Bearer " + userPref.getToken().toString())
                SettingViewModel.notificationTypeResponse.observe(this) {
                    if (it.status == 1) {
                        type = it.data.notification_status.toString()
                        if (type == "1") {
                            userPref.setNotificationType("5")
                            binding.switchButton.isChecked = true
                            binding.switchBtnTxtView.text = "On"
                        } else {
                            userPref.setNotificationType("5")
                            binding.switchButton.isChecked = false
                            binding.switchBtnTxtView.text = "Off"

                        }

                    } else {
                        toast(it.message)
                    }
                    userPref.setNotificationType("2")
                }






        SettingViewModel.notificationResponse.observe(this) {
            if (it.status == 1) {




            } else {

            }
        }

        SettingViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        if ( userPref.getNotificationType().equals("5")) {

        }else{
            binding.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    binding.switchBtnTxtView.text="On"
                    SettingViewModel.   notification_on_off(
                        "Bearer " + userPref.getToken().toString(),
                        "1",
                        userPref.getid().toString()
                    )
                } else {
                    binding.switchBtnTxtView.text="Off"
                    SettingViewModel.notification_on_off(
                        "Bearer " + userPref.getToken().toString(),
                        "0",
                        userPref.getid().toString()
                    )
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
           R.id.ivArrow ->{
               finish()
           }
        }
    }

}