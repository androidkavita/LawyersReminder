package com.lawyersbuddy.app.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityCreateNewPasswordBinding
import com.lawyersbuddy.app.databinding.FragmentOtpVerificationBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateNewPassword : BaseActivity() {
    lateinit var binding: ActivityCreateNewPasswordBinding
    private val signupViewModel: SignupViewModel by viewModels()
    var otp = ""
    var mobilenumber = ""
    private var counter = 60
    private var callTimer: Timer? = null

    var cDialogOtp: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_password)

        signupViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
        signupViewModel.errorString.observe(this) {
            toast(it)
        }
        signupViewModel.OtpResponse.observe(this) {
            if (it.status == 1) {
                toast(it.data.otp.toString())
                otp = it.data.otp.toString()
                otpDialog()

            } else {
                toast(it.message)
            }
        }
        binding.otpButton.setOnClickListener {
            mobilenumber = binding.mobileNumber.text.toString()
            if (binding.mobileNumber.text.toString().trim().isEmpty()) {
                toast("Please enter mobile number.")
            } else if (binding.mobileNumber.text.length <= 9) {
                toast("Please enter valid mobile number.")
            } else {
                signupViewModel.send_otp(mobilenumber)
            }

        }


    }

    private fun otpDialog() {
        cDialogOtp = Dialog(this, R.style.Theme_LawyersReminder)
        val dialogOtpBinding: FragmentOtpVerificationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.fragment_otp_verification, null, false
        )
        cDialogOtp?.setContentView(dialogOtpBinding.root)
        cDialogOtp?.setCancelable(false)
        cDialogOtp?.show()

        dialogOtpBinding.verify.setOnClickListener {

            if (dialogOtpBinding.otpView.text.toString().trim().isEmpty()) {
                toast("Please enter OTP.")
            } else if (dialogOtpBinding.otpView.text.toString() != otp) {
                toast("Please enter correct OTP.")
            } else {
                val intent = Intent(this, RecoverPasswordActivity::class.java).putExtra(
                    "mobilenumber",
                    mobilenumber
                )
                startActivity(intent)
                finish()

            }

        }
        dialogOtpBinding.tvResendOtp.setOnClickListener {
            signupViewModel.send_otp(mobilenumber)
            callTimer = Timer()
            countdown(dialogOtpBinding.otpTimer)
        }
        dialogOtpBinding.backButton.setOnClickListener {
            cDialogOtp?.dismiss()
            finish()
        }

        countdown(dialogOtpBinding.otpTimer)

//        cDialogOtp?.setOnKeyListener { arg0, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                cDialogOtp?.dismiss()
//                finish()
//            }
            true
//        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun countdown(countTv: TextView?) {

        object : CountDownTimer(10000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {

                if (millisUntilFinished / 1000 < 10) {
                    countTv?.text =
                        "(" + "00:0" + millisUntilFinished / 1000 + ")"/*"00:0" + millisUntilFinished / 1000*/

                } else {
                    countTv?.text = "(" + "00:" + millisUntilFinished / 1000 + ")"

                }
            }

            override fun onFinish() {
                countTv?.text = "(00:00)"
            }
        }.start()
    }

}