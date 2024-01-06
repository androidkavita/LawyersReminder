package com.lawyersbuddy.app.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.adapter.HearingAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.fragments.clicklistener.Click
import com.lawyersbuddy.app.databinding.ActivityHearingListBinding
import com.lawyersbuddy.app.model.HearingData
import com.lawyersbuddy.app.util.ApiConstant
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.HearingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HearingListActivity : BaseActivity(), Click, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityHearingListBinding
    private val hearingviewmodel: HearingViewModel by viewModels()
    private var list: ArrayList<HearingData> = ArrayList()
    lateinit var adapter: HearingAdapter
    var job: Job? = null
    var selectedCurrentDateFormat2 = ""
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hearing_list)

        hearingviewmodel.hearing_listApi("Bearer " + userPref.getToken().toString(), "", selectedCurrentDateFormat2)
        binding.arrow.setOnClickListener {
            finish()
        }
        binding.tvSelectedDate.text = "Start Date"
        hearingviewmodel.errorString.observe(this) {
            toast(it)
        }
        viewProgressStatus()

            binding.swipeRefreshlayout.setOnRefreshListener {
                if (binding.tvSelectedDate.text.equals("Start Date")) {
                    hearingviewmodel.hearing_listSearchApi("Bearer " + userPref.getToken().toString(), binding.edtSearch.text.toString(), "")
                    binding.swipeRefreshlayout.isRefreshing = false

                } else {
                    binding.swipeRefreshlayout.isRefreshing = false
                    hearingviewmodel.hearing_listSearchApi("Bearer " + userPref.getToken().toString(),  binding.edtSearch.text.toString(), selectedCurrentDateFormat2)


                }
        }

        hearingviewmodel.hearingListResponse.observe(this) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)
                binding.rvHearingList.visibility = View.VISIBLE
                binding.tvNoEvents.visibility = View.GONE
                adapter = HearingAdapter(this, list, this)
                binding.rvHearingList.adapter = adapter
                adapter.notifyDataSetChanged()

            } else {
                binding.rvHearingList.visibility = View.GONE
                binding.tvNoEvents.visibility = View.VISIBLE
            }
        }
        binding.edtSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500)

                it.let {
                    if (it.toString().isNotEmpty()) {
                        binding.searchImage.setImageResource(R.drawable.cross_icon)
                        hearingviewmodel.hearing_listSearchApi(
                            "Bearer " + userPref.getToken().toString(),
                            binding.edtSearch.text.trim().toString(),
                            selectedCurrentDateFormat2
                        )

                        binding.searchImage.setOnClickListener {
                            binding.edtSearch.text.clear()
                            hearingviewmodel.hearing_listSearchApi(
                                "Bearer " + userPref.getToken().toString(), "", selectedCurrentDateFormat2
                            )
                        }

                    } else {
                        binding.searchImage.setImageResource(R.drawable.icon_search)
                        hearingviewmodel.hearing_listSearchApi(
                            "Bearer " + userPref.getToken().toString(),
                            binding.edtSearch.text.trim().toString(),
                            selectedCurrentDateFormat2
                        )

                    }
                }
            }
        }

        binding.llStartDate.setOnClickListener {
            clickStartDatePicker()
        }
    }
    private fun viewProgressStatus() {
        hearingviewmodel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickStartDatePicker() {
        DatePickerDialog(
            this, this, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
//        val cal = Calendar.getInstance()
        val simpleDateFormat = android.icu.text.SimpleDateFormat("dd-MM-yyyy")

    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year, month, dayOfMonth)
//        cal1.set(year, month, dayOfMonth)
        displayFormattedDate(cal.timeInMillis)
//        displayFormattedFirstHearingDate(cal1.timeInMillis)
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        val simpleDateFormat2 = android.icu.text.SimpleDateFormat("dd-MM-yyyy")

        selectedCurrentDateFormat2 = simpleDateFormat2.format(timeInMillis)

        binding.tvSelectedDate.text = DateFormat.date(selectedCurrentDateFormat2)


        if (binding.tvSelectedDate.text.equals("Start Date")){

        }else{
            binding.calender.setImageResource(R.drawable.cross_icon)

            hearingviewmodel.hearing_listSearchApi(
                ApiConstant.Bearer + userPref.getToken().toString(),
                binding.edtSearch.text.toString(), selectedCurrentDateFormat2
            )
            binding.calender.setOnClickListener {
                binding.tvSelectedDate.text="Start Date"
                binding.calender.setImageResource(R.drawable.ic_date)
                selectedCurrentDateFormat2=""
                hearingviewmodel.hearing_listSearchApi(
                    ApiConstant.Bearer + userPref.getToken().toString(),
                    binding.edtSearch.text.toString(), ""
                )

            }
        }

    }

    override fun click(id: String) {
        startActivity(
            Intent(this, HearingDetailsActivity::class.java).putExtra("hearing_id", id)
                .putExtra("flag", "Hearing")
        )
    }

    override fun onResume() {
        super.onResume()
        hearingviewmodel.hearing_listApi("Bearer " + userPref.getToken().toString(), "", selectedCurrentDateFormat2)

    }
}