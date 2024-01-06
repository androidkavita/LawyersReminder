package com.lawyersbuddy.app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityCaseDetailsBinding
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.CasesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaseDetailsActivity : BaseActivity(), View.OnClickListener {
    var client_id = 0
    var newUrl = ""
    private val CaseItemViewModel: CasesViewModel by viewModels()
    lateinit var binding: ActivityCaseDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_case_details)

        if (intent != null) {
            client_id = intent.getIntExtra("id", 0)
        }
        CaseItemViewModel.errorString.observe(this) {
            toast(it)
        }
        binding.arrow.setOnClickListener(this)
        binding.llDownload.setOnClickListener(this)
        CaseItemViewModel.case_detail("Bearer " + userPref.getToken().toString(), client_id.toString())
        CaseItemViewModel.downloadPdfApi("Bearer " + userPref.getToken().toString(), client_id.toString())
        viewProgressStatus()

        viewCaseItemDetailResponse()
        viewDownloadCaseDetailResponse()
    }

    private fun viewDownloadCaseDetailResponse() {
        CaseItemViewModel.downloadCaseDetailResponse.observe(this) {
            if (it?.status == 1) {
//                snackbar(it.message)
                newUrl = it.data.file_path
            } else {
//                snackbar(it?.message!!)
            }
        }
    }

    private fun viewProgressStatus() {

        CaseItemViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }

    private fun viewCaseItemDetailResponse() {
        CaseItemViewModel.CasesDetailsResponse.observe(this) {
            if (it?.status == 1) {
                try {
                    binding.casesTitle.text = it.data.case_title
                    binding.judgeName.text = it.data.judge_name
                    binding.opponentparty.text = it.data.opponent_party_name
                    binding.startDate.text = DateFormat.NotificationDate(it.data.start_date)
                    binding.firsthearingdate.text = DateFormat.NotificationDate(it.data.first_hearing_date)
                    binding.courtName.text = it.data.court_name
                    binding.cnrNo.text = it.data.cnr_number
                    binding.clientName.text = it.data.client_name
                    binding.caseDetails.text = it.data.case_detail
                    binding.opponentlawyer.text = it.data.opponent_party_lawyer_name.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
//                snackbar(it?.message!!)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.arrow -> {
                finish()
            }
            R.id.llDownload -> {
             try {
                 startActivity(
                     Intent(
                         Intent.ACTION_VIEW, Uri.parse(
                             newUrl
                         )
                     )
                 )
             }catch (e:Exception){
                 e.printStackTrace()
             }
            }
        }
    }
}