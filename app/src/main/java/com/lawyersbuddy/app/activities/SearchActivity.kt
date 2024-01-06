package com.lawyersbuddy.app.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.adapter.CasesSearchAdapter
import com.lawyersbuddy.app.adapter.ClientSearchAdapter
import com.lawyersbuddy.app.adapter.HearingSearchAdapter

import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.fragments.clicklistener.Click
import com.lawyersbuddy.app.fragments.clicklistener.Clickcase
import com.lawyersbuddy.app.fragments.clicklistener.Clientclick
import com.lawyersbuddy.app.databinding.ActivitySearchBinding
import com.lawyersbuddy.app.model.HomeSearchData
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.CalenderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchActivity : BaseActivity(), View.OnClickListener , DatePickerDialog.OnDateSetListener,
    Clickcase, Click, Clientclick {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: CalenderViewModel by viewModels()
    private var list: ArrayList<HomeSearchData> = ArrayList()
    lateinit var adapter: CasesSearchAdapter
    lateinit var clientadapter: ClientSearchAdapter
    lateinit var hearingadapter: HearingSearchAdapter
    var job: Job? = null
    var type=""
    var cal = Calendar.getInstance()
    var cal1 = Calendar.getInstance()
    var checkdate=""
    var datepicker=""
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    var selectedCurrentDateFormat2 = ""
    var selectedHearingDateFormat1 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.ivBack.setOnClickListener(this)

        searchTextWatcherListener()
        viewSearchResponse()
        viewModel.errorString.observe(this) {
            toast(it)
        }

        binding.llenddate.setOnClickListener {
            clickHearingDatePicker()
        }
        binding.llStartdate.setOnClickListener {
            clickStartDatePicker()
        }
        binding.spinnerStateList.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                type="1"
               if (binding.spinnerStateList.selectedItem.equals("Clients")){
                   binding.dateselected.visibility=View.GONE
                   binding.rvHearingList.visibility=View.GONE
                   binding.rvCase.visibility=View.GONE
                   binding.rvClient.visibility=View.VISIBLE
                   type="1"
               }
                else if (binding.spinnerStateList.selectedItem.equals("Cases")){
                   binding.dateselected.visibility=View.VISIBLE
                   binding.rvHearingList.visibility=View.GONE
                   binding.rvCase.visibility=View.VISIBLE
                   binding.rvClient.visibility=View.GONE
                   type="2"
               }
                else{
                   binding.dateselected.visibility=View.VISIBLE
                   binding.rvHearingList.visibility=View.VISIBLE
                   binding.rvCase.visibility=View.GONE
                   binding.rvClient.visibility=View.GONE
                   type="3"
               }
                viewModel.home_searchApi(
                    "Bearer " + userPref.getToken().toString(),
                    binding.edtSearch.text.toString(),"","",type)
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickStartDatePicker() {
        checkdate="start"
        DatePickerDialog(
            this, this, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
//        val cal = Calendar.getInstance()
        val simpleDateFormat = android.icu.text.SimpleDateFormat("dd-MM-yyyy")
        datepicker="startdate"

    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        if ( checkdate.equals("start")){

            cal.set(year, month, dayOfMonth)
            if (datepicker.equals("startdate")){
                displayFormattedDate(cal.timeInMillis)
            }

        }
        else{
            cal1.set(year, month, dayOfMonth)
            if (datepicker.equals("startdate")){
                displayFormattedDate(cal1.timeInMillis)
            }
        }
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        val simpleDateFormat2 = android.icu.text.SimpleDateFormat("dd-MM-yyyy")
        selectedCurrentDateFormat2 = simpleDateFormat2.format(timeInMillis)
        binding.tvSelectedDate.text = DateFormat.date(selectedCurrentDateFormat2)
        if (binding.tvSelectedDate.text.equals("Start Date")){

        }else{
            binding.calender.setImageResource(R.drawable.cross_icon)
            viewModel.home_searchApi(
                "Bearer " + userPref.getToken().toString(),
                binding.edtSearch.text.toString(),selectedCurrentDateFormat2,selectedHearingDateFormat1,type)
            binding.calender.setOnClickListener {
                binding.tvSelectedDate.text="Start Date"
                binding.calender.setImageResource(R.drawable.ic_date)
                selectedCurrentDateFormat2=""

            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickHearingDatePicker() {
        checkdate="hearing"
        val simpleDateFormat2 = android.icu.text.SimpleDateFormat("dd-MM-yyyy")
        cal1.timeZone = TimeZone.getTimeZone("UTC")

        val datePickerDialog1 = DatePickerDialog(
            this, { view, year, monthOfYear, dayOfMonth ->
                cal1.set(year, monthOfYear, dayOfMonth)
                selectedHearingDateFormat1 = simpleDateFormat2.format(cal1.time)
                binding.tvEndDate.text = DateFormat.date(selectedHearingDateFormat1)

                if (binding.tvEndDate.text.equals("Start Date")){

                }else{
                    binding.calender1.setImageResource(R.drawable.cross_icon)

                    viewModel.home_searchApi(
                        "Bearer " + userPref.getToken().toString(),
                        binding.edtSearch.text.toString(),selectedCurrentDateFormat2,selectedHearingDateFormat1,type)
                    binding.calender1.setOnClickListener {
                        binding.tvEndDate.text="Start Date"
                        binding.calender1.setImageResource(R.drawable.ic_date)
                        selectedHearingDateFormat1=""

                    }
                }


            },
            cal1.get(Calendar.YEAR),
            cal1.get(Calendar.MONTH),
            cal1.get(Calendar.DAY_OF_MONTH)
        )
//        datePickerDialog1!!.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)

        datePickerDialog1.show()
    }


    private fun searchTextWatcherListener() {
        binding.edtSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500)

                it.let {

                        if (it.toString().isNotEmpty()) {

                            viewModel.home_searchApi(
                                "Bearer " + userPref.getToken().toString(),
                                binding.edtSearch.text.toString(),selectedCurrentDateFormat2,selectedHearingDateFormat1,type)

                                binding.searchImage.setImageResource(R.drawable.cross_icon)
                                binding.searchImage.setOnClickListener {
                                binding.edtSearch.text.clear()
                                viewModel.home_searchApi(
                                    "Bearer " + userPref.getToken().toString(),
                                 "",selectedCurrentDateFormat2,selectedHearingDateFormat1,type)

                            }

                        } else {
                            binding.searchImage.setImageResource(R.drawable.icon_search)
                            viewModel.home_searchApi(
                                "Bearer " + userPref.getToken().toString(),
                                "",selectedCurrentDateFormat2,selectedHearingDateFormat1,type)
                        }
                    }
                    }
                }
            }

    private fun viewSearchResponse() {
        viewModel.HomeSearchResponse.observe(this) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)
                binding.tvNoEvents.visibility = View.GONE
                if (binding.spinnerStateList.selectedItem.equals("Clients")) {
                    binding.rvCase.visibility=View.GONE
                    binding.rvClient.visibility=View.VISIBLE
                    binding.rvHearingList.visibility=View.GONE
                    binding.rvClient.layoutManager = LinearLayoutManager(this)
                    clientadapter = ClientSearchAdapter(this, list,this)
                    binding.rvClient.adapter = clientadapter
                    clientadapter.notifyDataSetChanged()
                }
                else if (binding.spinnerStateList.selectedItem.equals("Cases")){
                    binding.rvCase.visibility=View.VISIBLE
                    binding.rvClient.visibility=View.GONE
                    binding.rvHearingList.visibility=View.GONE
                    binding.rvCase.layoutManager = LinearLayoutManager(this)
                    adapter = CasesSearchAdapter(this, list,this)
                    binding.rvCase.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
               else{
                    binding.rvCase.visibility=View.GONE
                    binding.rvClient.visibility=View.GONE
                    binding.rvHearingList.visibility=View.VISIBLE
                    binding.rvHearingList.layoutManager = LinearLayoutManager(this)
                    hearingadapter = HearingSearchAdapter(this, list,this)
                    binding.rvHearingList.adapter = hearingadapter
                    hearingadapter.notifyDataSetChanged()
                }
            } else {
                binding.tvNoEvents.visibility = View.VISIBLE
                binding.tvNoEvents.text="No search results are found"
                binding.rvHearingList.visibility = View.GONE
                binding.rvCase.visibility = View.GONE
                binding.rvHearingList.visibility = View.GONE
            }
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun click(id: Int) {
        startActivity(Intent(this, CaseDetailsActivity::class.java).putExtra("id",id))

    }


    override fun Clientclick(id: Int) {

        startActivity(
            Intent(
                this,
                ClientItemDetailActivity::class.java
            ).putExtra("id", id)
        )    }

    override fun click(id: String) {
        startActivity(  Intent(this, HearingDetailsActivity::class.java).putExtra("hearing_id", id)
            .putExtra("flag", "Hearing"))
    }
}
