package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityRecoverPasswordBinding
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityRecoverPasswordBinding
    private val signupViewModel: SignupViewModel by viewModels()
    var mobilenumber=""
    private var isVisible2 = false
    private var isVisible1 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recover_password)

      if (intent!=null){
          mobilenumber=intent.getStringExtra("mobilenumber").toString()

      }
        signupViewModel.errorString.observe(this) {
            toast(it)
        }
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

        binding.passwordCrossEye.setOnClickListener {
            if (isVisible1) {
                binding.PasswordET.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.passwordCrossEye.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.cross_eye
                        )
                    )
                    binding.PasswordET.setSelection(binding.PasswordET.text.length)
                }
                isVisible1 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.PasswordET.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.passwordCrossEye.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.eye_solid
                        )
                    )
                    binding.PasswordET.setSelection(binding.PasswordET.text.length)
                }
                isVisible1 = true
            }
        }
        binding.confirmPasswordEye.setOnClickListener {
            if (isVisible2) {
                binding.confirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.confirmPasswordEye.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.cross_eye
                        )
                    )
                    binding.confirmPassword.setSelection(binding.confirmPassword.text.length)
                }
                isVisible2 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.confirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.confirmPasswordEye.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.eye_solid
                        )
                    )
                    binding.confirmPassword.setSelection(binding.confirmPassword.text.length)
                }
                isVisible2 = true
            }
        }
        signupViewModel.LoginResponse.observe(this){
            if (it.status ==1){
                toast("Password Reset Successfully")
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("flag","lawyer")
                startActivity(intent)
                finishAffinity()

            }
            toast(it.message)
        }
        binding.submit.setOnClickListener {
            if (binding.PasswordET.text.toString().trim().isEmpty()){
                toast(getString(R.string.please_enter_password))
            }

            else if (!CommonUtils.isValidPassword(
                    binding.PasswordET.text.toString().trim()
                )
            ) {  toast("Invalid password")
            }
            else if (binding.confirmPassword.text.toString().trim().isEmpty()){
              toast("Enter confirm password.")
            }
            else if (binding.confirmPassword.text.toString()!= binding.PasswordET.text.toString()){
              toast("Password and confirm password should be same.")
            }
            else{
                binding.confirmPasswordTv.visibility=View.GONE


                signupViewModel.forgot_password(mobilenumber,binding.PasswordET.text.toString())
            }

        }
    }
}