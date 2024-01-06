package com.lawyersbuddy.app.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityChangePasswordBinding
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {

    lateinit var binding: ActivityChangePasswordBinding
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()

    private var isVisible2 = false
    private var isVisible1 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)

        binding.arrow.setOnClickListener {
            finish()
        }

        changePasswordViewModel.changePasswordResponseModel.observe(this) {
            if (it.status == 1) {
                it.message?.let { it1 -> toast(it1) }
                userPref.clearPref()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("flag","lawyer")
                startActivity(intent)
            } else {
                it.message?.let { it1 -> toast(it1) }
            }

        }
        changePasswordViewModel.errorString.observe(this) {
            toast(it)
        }
        changePasswordViewModel.progressBarStatus.observe(this){
    if (it) {
        showProgressDialog()
    } else {
        hideProgressDialog()
    }
}
        binding.ivVisibleOldPass.setOnClickListener {
            if (isVisible1) {
                binding.etOldPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleOldPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.cross_eye
                        )
                    )
                    binding.etOldPassword.setSelection(binding.etOldPassword.text.length)
                }
                isVisible1 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.etOldPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleOldPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.eye_solid
                        )
                    )
                    binding.etOldPassword.setSelection(binding.etOldPassword.text.length)
                }
                isVisible1 = true
            }
        }

        binding.ivVisibleNewPass.setOnClickListener {
            if (isVisible1) {
                binding.etNewPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleNewPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.cross_eye
                        )
                    )
                    binding.etNewPassword.setSelection(binding.etNewPassword.text.length)
                }
                isVisible1 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.etNewPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleNewPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.eye_solid
                        )
                    )
                    binding.etNewPassword.setSelection(binding.etNewPassword.text.length)
                }
                isVisible1 = true
            }
        }

        binding.ivVisibleConfirmPass.setOnClickListener {
            if (isVisible2) {
                binding.etConfirmNewPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleConfirmPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.cross_eye
                        )
                    )
                    binding.etConfirmNewPassword.setSelection(binding.etConfirmNewPassword.text.length)
                }
                isVisible2 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.etConfirmNewPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleConfirmPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.eye_solid
                        )
                    )
                    binding.etConfirmNewPassword.setSelection(binding.etConfirmNewPassword.text.length)
                }
                isVisible2 = true
            }
        }

        binding.btnChangePassword.setOnClickListener {

            if (binding.etOldPassword.text.toString().trim().isEmpty()) {
                toast("Please Enter Old password.")
            } else if (binding.etNewPassword.text.toString().trim().isEmpty()) {
                toast("Please Enter New password.")
            } else if (!CommonUtils.isValidPassword(binding.etNewPassword.text.toString().trim())) {
                toast("Invalid Password.")
            } else if (binding.etConfirmNewPassword.text.toString().trim().isEmpty()) {
                toast("Enter confirm password.")
            } else if (binding.etConfirmNewPassword.text.toString() != binding.etNewPassword.text.toString()) {
                toast("Password and confirm password should be same.")
            } else {
                changePasswordViewModel.changePasswordApi(
                    "Bearer " + userPref.getToken().toString(),
                    binding.etOldPassword.text.toString(),
                    binding.etConfirmNewPassword.text.toString()
                )
                Log.d(TAG, "onCreate: " + userPref.getToken().toString())
            }
        }
    }
}