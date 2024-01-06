package com.lawyersbuddy.app.activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.lawyersbuddy.app.CommonMethod.CommonMethods
import com.lawyersbuddy.app.Permissions.CallbackManager
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityLoginBinding
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.CommonUtils.containsAnyOfIgnoreCase
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    private val loginViewModel: SignupViewModel by viewModels()
    private var isVisible1 = false
    var flag = ""
    var type = ""
    private val RC_SIGN_IN = 9
    var socialId: String = ""
    var firstName = ""
    var googleemail = ""
    var verification = false

    // lateinit var userPref: UserPref
//    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    var lastname = ""
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var callbackManager: CallbackManager
    lateinit var mContext: Context
    var token = ""
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        mContext = this.applicationContext
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            token = task.result

        })
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, mGoogleSignInOptions)
        FirebaseApp.initializeApp(this)
        callbackManager = CallbackManager.Factory.create()

        binding.loginGoogle.setOnClickListener {
            if (CommonMethods.phoneIsOnline(mContext)) {
                val signInIntent: Intent? = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            } else {
                toast(mContext, "Internet connection not available!")
            }
            binding.etEmail.setText(googleemail)

        }
             binding.forgotPassword.setOnClickListener {
            if (flag.equals("lawyer")) {
                val intent = Intent(this, CreateNewPassword::class.java)
                startActivity(intent)
            } else {
                supportFragmentManager.let {
                    ForgotPasswordAssistant().show(
                        it,
                        "MyCustomFragment"
                    )
                }
            }
        }

        loginViewModel.user_detailResponse.observe(this) {
            if (it.status == 1) {
                if (it.data.is_user == 1) {
                    val intent = Intent(this, RegisterActivity::class.java)
                    intent.putExtra("flag", "lawyer")
                    intent.putExtra("verification", "noverification")
                    intent.putExtra("email", it.data.email)
                    intent.putExtra("flag1", "login")
                    startActivity(intent)
                } else {
                    loginViewModel.google_login(
                        "1",
                        googleemail,
                        socialId,
                        firstName + " " + lastname,
                        "qwer",
                        token
                    )
                }
            } else {

            }
        }
        loginViewModel.errorString.observe(this) {
            toast(it)
        }

        if (intent != null) {
            flag = intent.getStringExtra("flag").toString()
        }
        if (flag.equals("lawyer")) {
            type = "1"
            binding.signupLayout.visibility = View.VISIBLE
            binding.loginTv.text = "Advocate Login"
        } else {
            type = "2"
            binding.signupLayout.visibility = View.GONE
            binding.loginTv.text = "Assistant Login"
        }
        loginViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        binding.crossEye.setOnClickListener {
            if (isVisible1) {
                binding.PasswordET.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.crossEye.setImageDrawable(
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
                    binding.crossEye.setImageDrawable(
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
        loginViewModel.LoginResponse.observe(this) {
            if (it.status == 1) {
                val intent = Intent(this, MainActivity::class.java)

                userPref.isLogin = true
                userPref.setid(it.data.id.toString())
                userPref.setToken(it.data.token)
                userPref.setName(it.data.name)
                userPref.setEmail(it.data.email)
                userPref.setid(it.data.id.toString())
                userPref.setProfileImage(it.data.image.toString())
                userPref.setMobile(it.data.mobile)
                userPref.setBarAssociationNumber(it.data.bar_association)
                userPref.setBarCouncilNumber(it.data.bar_council_number)
                userPref.setState(it.data.state)
                userPref.setCity(it.data.city)
                userPref.setPermission(it.data.is_permission.toString())
                userPref.setType(it.data.type.toString())
                userPref.setStateId(it.data.state_id.toString())
                userPref.setCityId(it.data.city_id.toString())


                //  userPref.setAddress(it.data.address)
                startActivity(intent)
                finishAffinity()
            } else {
                toast(it.message)
            }
        }
        loginViewModel.LoginResponse_google.observe(this) {
            if (it.status == 1) {
                val intent = Intent(this, MainActivity::class.java)

                userPref.isLogin = true
                userPref.setid(it.data.id.toString())
                userPref.setToken(it.data.token)
                userPref.setName(it.data.name)
                userPref.setEmail(it.data.email)
                userPref.setid(it.data.id.toString())
                userPref.setProfileImage(it.data.image.toString())
                userPref.setMobile(it.data.mobile)
                userPref.setBarAssociationNumber(it.data.bar_association)
                userPref.setBarCouncilNumber(it.data.bar_council_number)
                userPref.setState(it.data.state)
                userPref.setCity(it.data.city)
                userPref.setNotificationType(it.data.notification_status.toString())
                userPref.setPermission(it.data.is_permission.toString())
                userPref.setType(it.data.type.toString())
                userPref.setStateId(it.data.state_id.toString())
                userPref.setCityId(it.data.city_id.toString())
                //  userPref.setAddress(it.data.address)
                startActivity(intent)
                finishAffinity()
            } else {
                toast(it.message)
            }
        }
        binding.signup.setOnClickListener {
            if (flag.equals("lawyer")) {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("flag", "lawyer")
                startActivity(intent)
            } else {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("flag", "assistant")
                startActivity(intent)
            }
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etEmail.text.toString().trim().isEmpty()) {
                toast(getString(R.string.please_enter_email_address))
            } else if (!CommonUtils.isValidMail(binding.etEmail.text.toString().trim())) {
                toast(getString(R.string.please_enter_valid_email_address))
            } else if (!binding.etEmail.text.toString()
                    .containsAnyOfIgnoreCase(listOf(".net", ".com", ".org", ".in", ".eu", ".io"))
            ) {
                toast(getString(R.string.please_enter_valid_domain))
            } else if (binding.PasswordET.text.toString().trim().isEmpty()) {
                toast(getString(R.string.please_enter_password))
            } else if (!CommonUtils.isValidPassword(binding.PasswordET.text.toString().trim())) {
                toast(getString(R.string.please_enter_valid_password))
            } else {

                if (flag.equals("lawyer")){
                    if (userPref.getnoverification().equals("no")){
                        loginViewModel.login(   binding.etEmail.text.toString(),
                            binding.PasswordET.text.toString(),
                            type, token)
                    }else{
                        signInUser()
                    }

                }
                else{
                    loginViewModel.login(   binding.etEmail.text.toString(),
                        binding.PasswordET.text.toString(),
                        type, token)
                }

            }
        }

    }

    private fun signInUser() {
        firebaseAuth.signInWithEmailAndPassword(binding.etEmail.text.toString(),"Kavi@017")
            .addOnCompleteListener { signIn ->
                if (signIn.isSuccessful) {
                    isverified()
                    }else {
                        toast("Invalid credentials.")
                }
            }
    }

    fun isverified(){
        if (firebaseUser!!.isEmailVerified()) {
            RegisterActivity.verification =true
            loginViewModel.login(
                binding.etEmail.text.toString(),
                binding.PasswordET.text.toString(),
                type, token
            )
         }
        else {
            RegisterActivity.verification =false
            Toast.makeText(this, "User isn't verified...", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                RC_SIGN_IN -> {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        firstName = account.givenName.toString()
                        if (account.familyName != null)
                            lastname = account.familyName.toString()
                        googleemail = account.email.toString()
                        socialId = account.id.toString()
                        //   val personPhoto = account.getPhotoUrl()!!.toString()

                        Log.d("socialId", "msd".plus(socialId).plus(firstName) + "  " + lastname)
//                        CommonMethods.googleLogout(this)
                        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                            if (it.isComplete) {
                                try {
                                    val firebaseToken = it.result.toString()


                                    loginViewModel.user_detail(googleemail)
                                } catch (e: Exception) {
                                }
                            }
                        }

                    } catch (e: ApiException) {
                        if (CommonMethods.phoneIsOnline(mContext)) {
                            toast(mContext, "Google Sign In Cancel")
                        } else {
                            toast(mContext, "Internet connection not available")
                        }
                        Log.e("GOOGLE_SIGNIN: else", "catch")
                    }
                }
            }
        } else {
            Log.e("account", "accountTyopeerror")
        }
    }
}
