package com.lawyersbuddy.app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.databinding.ActivityChooseRoleBinding

class ChooseRoleActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseRoleBinding
    var background=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_role)




        binding.advocate.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            background="advocate"
            intent.putExtra("flag","lawyer")
            startActivity(intent)
//            finishAffinity()
        }
        binding.assistant.setOnClickListener {
            background="assistant"
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("flag","assistant")
            startActivity(intent)
//            finishAffinity()
        }


    }
}