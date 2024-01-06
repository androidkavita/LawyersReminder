package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityRegisterBinding
import com.lawyersbuddy.app.model.CityListData
import com.lawyersbuddy.app.model.StateListData
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.CommonUtils.containsAnyOfIgnoreCase
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.SignupViewModel
import com.lawyersbuddy.app.viewmodel.StateListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : BaseActivity(), View.OnClickListener {
    private val stateViewModel: StateListViewModel by viewModels()
    private val signupViewModel: SignupViewModel by viewModels()
    lateinit var binding: ActivityRegisterBinding
    var verification=""
    lateinit var statelist: ArrayList<StateListData>
    lateinit var cityList: ArrayList<CityListData>
    var state: ArrayList<String> = ArrayList()
    var city: ArrayList<String> = ArrayList()
    var state_id: ArrayList<String> = ArrayList()
    var stateId = ""
    var email = ""
    var check = false
    var email1=""
    var password=""
    private var isVisible2 = false
    private var isVisible1 = false
    var flag = ""
    var flag1 = ""
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    companion object{
        var verification=false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        statelist = ArrayList()
        cityList = ArrayList()
        setOnClickListener()
        email = binding.email.text.toString()

        if (intent != null) {
            flag = intent.getStringExtra("flag").toString()
            verification = intent.getStringExtra("verification").toString()
            flag1 = intent.getStringExtra("flag1").toString()
            email = intent.getStringExtra("email").toString()
        }
        if (verification.equals("noverification")){
            userPref.setnoverification("no")
        }
        if (flag1.equals("login")) {
            binding.email.setText(email)
            binding.email.isEnabled = false
        } else {
            binding.email.isEnabled = true

        }


        binding.stateList.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stateId = state_id[p2]
                cityList.clear()
                stateViewModel.city_list(stateId)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        binding.cityList.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        stateViewModel.StateListApi()
        signupViewModel.errorString.observe(this) {
            toast(it)
        }
        stateViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        signupViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        signupViewModel.SignupResponseModel.observe(this) {
            if (it.status == 1) {
                if (flag.equals("lawyer")) {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("flag", "lawyer")
                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("flag", "assistant")
                    startActivity(intent)
                }
            }
            toast(it.message)
        }
        stateViewModel.CityListResponseModel.observe(this) {
            if (it?.status == 1) {
                cityList.clear()
                city.clear()
                stateId = ""
                cityList.addAll(it.data)
                stateViewModel.CityListData.value = it.data
                for (i in 0 until it.data.size) {
                    city.add(it.data[i].city)
                }
                val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    city
                )
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.cityList.adapter = spinnerArrayAdapter

            } else {
                //toast(it.message)
                snackbar(it?.message!!)
            }
        }
        stateViewModel.StateListResponseModel.observe(this) {
            if (it?.status == 1) {
                statelist.clear()
                statelist.addAll(it.data)
                stateViewModel.StateListData.value = it.data
                for (i in 0 until it.data.size) {
                    state.add(it.data[i].state)
                    state_id.add(it.data[i].id.toString())
                }
                val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    state
                )
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.stateList.adapter = spinnerArrayAdapter


            } else {
                //toast(it.message)
                snackbar(it?.message!!)
            }
        }


    }


    private fun signIn() {
        email1 = binding.email.text.toString()
        password="Kavi@017"
        firebaseAuth.createUserWithEmailAndPassword(email1,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification()


                    } else {

                        toast("Already have an account!")
                    }
                }

    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Email sent to ${binding.email.text}")
                    signupViewModel.SignupApi(
                        binding.FirstNameET.text.toString(),
                        binding.email.text.toString(),
                        binding.PasswordET.text.toString(),
                        binding.mobileNumber.text.toString(),
                        binding.cityList.selectedItem.toString(),
                        binding.stateList.selectedItem.toString(),
                        binding.barAssociation.text.toString(),
                        binding.barCouncilNumber.text.toString(),
                        "1",
                        binding.FirstNameET.text.toString(),
                        binding.address.text.toString()
                    )
                }
            }
        }
    }



    private fun setOnClickListener() {
        binding.checkbox.setOnClickListener(this)
        binding.checked.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)
        binding.passwordCrossEye.setOnClickListener(this)
        binding.confirmPasswordEye.setOnClickListener(this)
        binding.submitButton.setOnClickListener(this)
        binding.termsAndConditions.setOnClickListener(this)
        binding.llLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llLogin -> {
//                firebase()
                finish()
            }
            R.id.terms_and_conditions -> {
                val intent = Intent(this, TermsandConditionsActivity::class.java)
                startActivity(intent)
            }
            R.id.checkbox -> {
                binding.checked.visibility = View.VISIBLE
                check = true
            }
            R.id.checked -> {
                binding.checked.visibility = View.GONE
                check = false
            }
            R.id.back_button -> {
                finish()
            }
            R.id.passwordCrossEye -> {
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
            R.id.confirmPasswordEye -> {
                if (isVisible2) {
                    binding.cfPasswordET.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.confirmPasswordEye.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this,
                                R.drawable.cross_eye
                            )
                        )
                        binding.cfPasswordET.setSelection(binding.cfPasswordET.text.length)
                    }
                    isVisible2 = false
                } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                    binding.cfPasswordET.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.confirmPasswordEye.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this,
                                R.drawable.eye_solid
                            )
                        )
                        binding.cfPasswordET.setSelection(binding.cfPasswordET.text.length)
                    }
                    isVisible2 = true
                }
            }
            R.id.submit_button -> {

                if (binding.FirstNameET.text.toString().trim().isEmpty()) {
                    toast("Enter full name.")
                } else if (binding.email.text.toString().trim().isEmpty()) {
                    toast("Enter email id.")
                } else if (!CommonUtils.isValidMail(binding.email.text.toString().trim())) {
                    toast(getString(R.string.please_enter_valid_email_address))
                } else if (!binding.email.text.toString()
                        .containsAnyOfIgnoreCase(
                            listOf(
                                ".net",
                                ".com",
                                ".org",
                                ".in",
                                ".eu",
                                ".io"
                            )
                        )
                ) {
                    toast(getString(R.string.please_enter_valid_domain))
                } else if (binding.barAssociation.text.toString().trim().isEmpty()) {
                    toast("Enter bar association.")
                } else if (binding.barCouncilNumber.text.toString().trim().isEmpty()) {
                    toast("Enter bar council number.")
                } else if (binding.mobileNumber.text.toString().trim().isEmpty()) {
                    toast("Enter mobile number.")
                } else if (binding.mobileNumber.text.length <= 9) {
                    toast("Enter valid mobile number.")
                } else if (binding.zipcode.toString().trim().isEmpty()) {
                    toast(getString(R.string.please_enter_zipcode))
                } else if (binding.zipcode.toString().trim().length <= 5) {
                    toast(getString(R.string.please_enter_valid_zipcode))
                } else if (binding.PasswordET.text.toString().trim().isEmpty()) {
                    toast(getString(R.string.please_enter_password))
                } else if (!CommonUtils.isValidPassword(
                        binding.PasswordET.text.toString().trim()
                    )
                ) {
                    toast("Invalid password")
                } else if (binding.cfPasswordET.text.toString().trim().isEmpty()) {
                    toast("Enter confirm password.")
                } else if (binding.cfPasswordET.text.toString() != binding.PasswordET.text.toString()) {
                    toast("Please enter Password and confirm password should be same.")
                } else if (check == false) {
                    toast("Please accept terms and conditions.")
                } else {
                    if (verification.equals("noverification")){
                        signupViewModel.SignupApi(
                            binding.FirstNameET.text.toString(),
                            binding.email.text.toString(),
                            binding.PasswordET.text.toString(),
                            binding.mobileNumber.text.toString(),
                            binding.cityList.selectedItem.toString(),
                            binding.stateList.selectedItem.toString(),
                            binding.barAssociation.text.toString(),
                            binding.barCouncilNumber.text.toString(),
                            "1",
                            binding.FirstNameET.text.toString(),
                            binding.address.text.toString()
                        )
                    }else{
                        signIn()
                    }


                }
            }

        }
    }


}
