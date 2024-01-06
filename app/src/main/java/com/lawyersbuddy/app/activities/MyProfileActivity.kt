package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.fragments.clicklistener.deleteaccount
import com.lawyersbuddy.app.databinding.ActivityMyProfileBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.GetProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileActivity : BaseActivity() , deleteaccount {
    private lateinit var binding: ActivityMyProfileBinding
    private val viewModel: GetProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)

        viewModel.get_profileApi("Bearer " + userPref.getToken().toString())

        binding.arrow.setOnClickListener {
            finish()
        }
        viewModel.errorString.observe(this) {
            toast(it)
        }
        binding.llEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
        }


        if (userPref.getType().equals("1")){
            binding.llEditProfile.visibility=View.VISIBLE
            binding.gone.visibility=View.GONE
            binding.additional.visibility=View.VISIBLE
        }
        else{
            binding.llEditProfile.visibility=View.GONE
             binding.gone.visibility=View.VISIBLE
            binding.additional.visibility=View.GONE
        }
        binding.llChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        viewModel.editProfileViewResponse.observe(this) {
            if (it.status == 1) {
                userPref.isLogin=false
                userPref.clearPref()
                finishAffinity()
                startActivity(Intent(this, ChooseRoleActivity::class.java))
            }
            }

        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        viewProfileResponse()
        binding.deleteAccount.setOnClickListener {
            supportFragmentManager?.let { DeleteAccountConfirmation(this,userPref.getid().toString()).show(it, "MyCustomFragment") }

        }

    }

    private fun viewProfileResponse() {
        viewModel.profileViewResponse.observe(this) {
            if (it.status == 1) {

                Glide.with(this).load(it.data?.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                    .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                    .into(binding.ivPict)

                if (it.data!!.firmName == null) {
                    binding.tvFirmName.text = "......"
                } else {
                    binding.tvFirmName.text = it.data!!.firmName.toString()
                }
                if (it.data!!.address == null) {
                    binding.tvAddress.text = "......"
                } else {
                    binding.tvAddress.text = it.data!!.address.toString()
                    /*it.data!!.state.toString() +","+it.data!!.city.toString()*/
                }

                if (it.data!!.barAssociation == null) {
                    binding.tvBarAssociation.text = "......"
                } else {
                    binding.tvBarAssociation.text = it.data!!.barAssociation.toString()
                }

                if (it.data!!.barCouncilNumber == null) {
                    binding.tvBarCouncilNumber.text = "......"
                } else {
                    binding.tvBarCouncilNumber.text = it.data?.barCouncilNumber.toString()
                }

                binding.tvFullName.text = it.data?.name.toString()
                binding.tvNumber.text = it.data?.mobile.toString()
                binding.tvEmail.text = it.data?.email.toString()


                /*    if (it.data?.image != null) {
                        userPref.setProfileImage(it.data?.image)
                        Log.e("@@image", userPref.getUserProfileImage().toString())
                    } else {
                        userPref.setProfileImage("")
                    }*/


            } else {
                Log.d("Response", it.toString())
                toast(this, it.message!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.get_profileApi("Bearer " + userPref.getToken().toString())
        viewProfileResponse()
    }

    override fun deleteaccount(id: String?) {
        viewModel.delete_user("Bearer " + userPref.getToken().toString(),id!!)
    }
}