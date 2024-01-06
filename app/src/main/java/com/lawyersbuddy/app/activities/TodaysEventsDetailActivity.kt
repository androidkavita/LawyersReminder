package com.lawyersbuddy.app.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil

import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityTodaysEventsDetailBinding
import com.lawyersbuddy.app.viewmodel.CalenderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodaysEventsDetailActivity : BaseActivity() {
    var event_id = ""
    private val viewModel: CalenderViewModel by viewModels()
    lateinit var binding: ActivityTodaysEventsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todays_events_detail)

        if (intent != null) {
            event_id = intent.getStringExtra("event_id").toString()
        }
        binding.arrow.setOnClickListener {
            finish()
        }
//        viewModel.case_detail("Bearer " + userPref.getToken().toString(), event_id)

        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        /*viewModel.CasesDetailsResponse.observe(this) {
            if (it?.status == 1) {
                try {
                    binding.casesTitle.text = it.data.case_title
                    binding.judgeName.text = it.data.judge_name
                    binding.opponentparty.text = it.data.opponent_party_name
                    binding.startDate.text = it.data.start_date
                    binding.firsthearingdate.text = it.data.first_hearing_date
                    binding.courtName.text = it.data.court_name
                    binding.cnrNo.text = it.data.cnr_number
                    binding.caseDetails.text = it.data.case_detail
                    binding.opponentlawyer.text = it.data.opponent_party_lawyer_name.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                snackbar(it?.message!!)
            }*/
       // }
    }
}