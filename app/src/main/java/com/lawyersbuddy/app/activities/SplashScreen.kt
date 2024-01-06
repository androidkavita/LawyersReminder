package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.prefs.UserPref

class SplashScreen : BaseActivity() {
    private  val splashTimeout : Long = 1000 //1sec


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            var token = task.result
        })*/
        Handler().postDelayed({
            val userPref = UserPref(this)

            if(userPref.isLogin){
                      val intent = Intent(this, MainActivity::class.java)
                      startActivity(intent)
                      finishAffinity()
                  }else {
                      val intent = Intent(this, ChooseRoleActivity::class.java)
                      // val intent = Intent(this, UserDashActivity::class.java)
                      startActivity(intent)
                      finishAffinity()
                  }


        },splashTimeout)
    }
}