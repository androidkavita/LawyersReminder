package com.lawyersbuddy.app.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.fragments.clicklistener.deleteaccount

class DeleteAccountConfirmation  (var deleteaccount: deleteaccount, var id:String?) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.delete_account_confirmation, container, false)
        val yes_btn = view.findViewById<TextView>(R.id.btnconfirm)

        yes_btn.setOnClickListener {
            deleteaccount.deleteaccount(id)
            dismiss()
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }



}