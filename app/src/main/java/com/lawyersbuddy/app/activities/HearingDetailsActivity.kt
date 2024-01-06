package com.lawyersbuddy.app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.adapter.ImageAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.fragments.clicklistener.ClickIMage
import com.lawyersbuddy.app.fragments.clicklistener.clickpdf
import com.lawyersbuddy.app.databinding.ActivityHearingDetailsBinding
import com.lawyersbuddy.app.model.file_detail
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.HearingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HearingDetailsActivity : BaseActivity(), View.OnClickListener, ClickIMage, clickpdf {
    lateinit var binding: ActivityHearingDetailsBinding
    var hearing_id = ""
    var flag = ""
    var event_id = ""
    var id = ""
    var downloadUrl = ""
    lateinit var adapter: ImageAdapter
    private var list: ArrayList<file_detail> = ArrayList()
    private val hearingViewModel: HearingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hearing_details)

        if (intent != null) {
            hearing_id = intent.getStringExtra("hearing_id").toString()
            flag = intent.getStringExtra("flag").toString()
            event_id = intent.getStringExtra("event_id").toString()
        }

        hearingViewModel.errorString.observe(this) {
            toast(it)
        }
        binding.arrow.setOnClickListener(this)
        if (userPref.getType().equals("1")) {
            binding.edit.visibility = View.VISIBLE
        } else {
            if (userPref.getPermission().equals("0")) {
                binding.edit.visibility = View.GONE
            } else {
                binding.edit.visibility = View.VISIBLE
            }
        }
        if (flag.equals("Hearing")) {
            hearingViewModel.hearing_detailApi(
                "Bearer " + userPref.getToken().toString(),
                hearing_id
            )
            id = hearing_id

        } else {
            hearingViewModel.hearing_detailApi("Bearer " + userPref.getToken().toString(), event_id)
            id = event_id
        }
        hearingViewModel.downloadHearingDetailApi("Bearer " + userPref.getToken().toString(), id)
        viewProgressStatus()
        viewHearingDetailResponse()
        binding.edit.setOnClickListener(this)
        binding.llDownload.setOnClickListener(this)
        viewDownloadPdfResponse()
    }

    private fun viewDownloadPdfResponse() {
        hearingViewModel.downloadHearingDetailsResponse.observe(this) {
            if (it?.status == 1) {
               downloadUrl= it.data?.file_path.toString()
            }
            else {
                snackbar(it?.message!!)
            }
        }
    }
    private fun viewHearingDetailResponse() {
        hearingViewModel.hearingDetailsResponse.observe(this) {
            if (it?.status == 1) {
                try {
                    list.clear()
                    list.addAll(it.data?.file_detail!!)
                    adapter = ImageAdapter(this,list,this,this)
                    binding.rvImages.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.tvPartyName.text = it.data?.clientName
                    binding.tvCaseTitle.text = it.data?.caseTitle
                    binding.hearingDate.text = DateFormat.dateHearing(it.data?.currentDate)

                    if (it.data?.final_date==0){
                        binding.tvFinalHearing.text="No"
                        binding.tvNexthearingdate.text = DateFormat.dateHearing(it.data?.hearingDate)
                    }
                    else{
                        binding.edit.visibility=View.GONE
                        binding.tvFinalHearing.text="Yes"
                        binding.tvNexthearingdate.text = "Case Closed"
                    }
                    binding.tvCaseStage.text = it.data?.caseStage
                    binding.tvCaseStatus.text = it.data?.caseStatus
                    binding.tvHearingDetails.text = it.data?.hearingDetails

                /* if (it.data!!.editStatus == "1"){
                     binding.edit.isEnabled=false
                     binding.edit.isClickable= true

                 }else{
                     binding.edit.isEnabled=true
                     binding.edit.isClickable= false
                 }*/

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                snackbar(it?.message!!)
            }
        }
    }

    private fun viewProgressStatus() {

        hearingViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (flag.equals("Hearing")) {
            hearingViewModel.hearing_detailApi(
                "Bearer " + userPref.getToken().toString(),
                hearing_id
            )
        } else {
            hearingViewModel.hearing_detailApi("Bearer " + userPref.getToken().toString(), event_id)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit -> {
                startActivity(
                    Intent(this, AddNewHearingActivity::class.java).putExtra(
                        "hearing_id",
                        id
                    )
                )
            }
            R.id.arrow -> {
                finish()
            }
            R.id.llDownload -> {
                try{
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                downloadUrl
                            )
                        )
                    )
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    override fun click(image: String?) {
        supportFragmentManager.let { PopupActivity(image).show(it,"MyCustomFragment") }
    }
    override fun clickpdf(image: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(image)))
        toast("Hearing Downloaded successfully!")
    }
}