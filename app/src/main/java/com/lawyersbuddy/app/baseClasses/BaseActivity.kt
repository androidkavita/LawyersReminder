package com.lawyersbuddy.app.baseClasses

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lawyersbuddy.app.R

import com.google.android.material.snackbar.Snackbar
import com.lawyersbuddy.app.prefs.UserPref
import com.lawyersbuddy.app.util.Utils
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var userPref: UserPref

    @Inject
    lateinit var utils: Utils

    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        userPref = UserPref(this)
    }

    @SuppressLint("ResourceAsColor")
    protected fun showProgressDialog() {
        if (dialog == null)
            dialog = Dialog(this)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        //  dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(droidninja.filepicker.R.color.transparent_black));

        if (dialog != null && !dialog!!.isShowing)
            dialog!!.show()
    }

    protected fun hideProgressDialog() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }


    protected fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flContent, fragment, fragment.javaClass.name).commit()
    }

    fun changeLanguage(prefLanguage: String) {
        val locale = Locale(prefLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun snackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
