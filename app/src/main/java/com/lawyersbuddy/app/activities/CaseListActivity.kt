package com.lawyersbuddy.app.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.adapter.CasesListAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.fragments.clicklistener.Clickcase
import com.lawyersbuddy.app.fragments.clicklistener.caseedit
import com.lawyersbuddy.app.databinding.ActivityCaseListBinding
import com.lawyersbuddy.app.model.CasesData
import com.lawyersbuddy.app.util.ApiConstant
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.CasesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CaseListActivity : BaseActivity(),
    View.OnClickListener , Clickcase, caseedit {
    lateinit var binding: ActivityCaseListBinding
    private val viewModel: CasesViewModel by viewModels()
    private var list1: ArrayList<CasesData> = ArrayList()
    lateinit var adapter: CasesListAdapter

    var job: Job? = null
    var selectedCurrentDateFormat1 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_case_list)

        viewModel.CasesListApi("Bearer " + userPref.getToken().toString(), "", "")

        binding.swipeRefreshlayout.setOnRefreshListener {

            if (binding.tvSelectedDate.text.equals("Start Date")) {

                viewModel.CasesListSearchApi("Bearer " + userPref.getToken().toString(),  binding.edtSearch.text.toString(), "")
                binding.swipeRefreshlayout.isRefreshing=false

            }else{
                viewModel.CasesListSearchApi("Bearer " + userPref.getToken().toString(), binding.edtSearch.text.toString(), selectedCurrentDateFormat1)
                binding.swipeRefreshlayout.isRefreshing=false
            }


        }
        if (userPref.getType().equals("1")) {
            binding.addCase.visibility = View.VISIBLE
        } else {
            if (userPref.getPermission().equals("0")) {
                binding.addCase.visibility = View.GONE
            } else {
                binding.addCase.visibility = View.VISIBLE

            }
        }
        setOnClickListener()
        viewProgressStatus()

        viewCaseResponse()

        viewModel.errorString.observe(this) {
            toast(it)
        }

        binding.llStartDate.setOnClickListener {
            clickStartDatePicker()
        }

        binding.edtSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500)

                it.let {
                    if (it.toString().isNotEmpty()) {
                    binding.searchImage.setImageResource(R.drawable.cross_icon)
                        viewModel.CasesListSearchApi("Bearer " + userPref.getToken().toString(),binding.edtSearch.text.trim().toString(), selectedCurrentDateFormat1)

                        binding.searchImage.setOnClickListener {
                            binding.edtSearch.text.clear()
                            viewModel.CasesListSearchApi("Bearer " + userPref.getToken().toString(), "", selectedCurrentDateFormat1)

                        }

                    } else {
                        viewModel.CasesListSearchApi("Bearer " + userPref.getToken().toString(),  binding.edtSearch.text.trim().toString(), selectedCurrentDateFormat1)
                        binding.searchImage.setImageResource(R.drawable.icon_search)
                    }
                }
            }
        }

    }

    private fun setOnClickListener() {
        binding.llSearch.setOnClickListener(this)
        binding.arrow.setOnClickListener(this)
        binding.addNewClient.setOnClickListener(this)
    }

    private fun viewProgressStatus() {
        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }

    private fun viewCaseResponse() {
        viewModel.CasesListResponse.observe(this) {
            if (it?.status == 1) {
                list1.clear()
                list1.addAll(it.data)
                binding.rvCaseList.visibility = View.VISIBLE
                binding.tvNoEvents.visibility = View.GONE
                adapter = CasesListAdapter( this,list1,userPref,this,this)
                binding.rvCaseList.adapter = adapter
                adapter.notifyDataSetChanged()

            } else {
                binding.rvCaseList.visibility = View.GONE
                binding.tvNoEvents.visibility = View.VISIBLE
            }
        }

    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickStartDatePicker() {
        val cal = Calendar.getInstance()
        val simpleDateFormat = android.icu.text.SimpleDateFormat("dd-MM-yyyy")
        cal.timeZone = TimeZone.getTimeZone("UTC")

        val datePickerDialog = DatePickerDialog(
            this, { view, year, monthOfYear, dayOfMonth ->
                cal.set(year, monthOfYear, dayOfMonth)
                selectedCurrentDateFormat1 = simpleDateFormat.format(cal.time
                )

                binding.tvSelectedDate.text = DateFormat.date(selectedCurrentDateFormat1)

                if (binding.tvSelectedDate.text.equals("Start Date")){

                }else{
                    binding.calender.setImageResource(R.drawable.cross_icon)

                    viewModel.CasesListSearchApi(
                        ApiConstant.Bearer + userPref.getToken().toString(),
                        binding.edtSearch.text.toString(), selectedCurrentDateFormat1
                    )

                    binding.calender.setOnClickListener {
                        binding.tvSelectedDate.text="Start Date"
                        binding.calender.setImageResource(R.drawable.ic_date)
                        selectedCurrentDateFormat1=""
                        viewModel.CasesListSearchApi(
                            ApiConstant.Bearer + userPref.getToken().toString(),
                            binding.edtSearch.text.toString() ,""
                        )

                    }
                }


            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
//        datePickerDialog.datePicker. = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.CasesListApi("Bearer " + userPref.getToken().toString(), "", "")
//
//    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.add_new_client -> {
                startActivity(Intent(this, AddNewCaseActivity::class.java))
                intent.putExtra("flag","add")


            }
            R.id.arrow -> {
                finish()
            }
        }
    }
    override fun click(id: Int) {
        startActivity(Intent(this, CaseDetailsActivity::class.java).putExtra("id",id))
    }
    override fun clickedit(id: Int) {
        startActivity(Intent(this, AddNewCaseActivity::class.java).putExtra("id",id).putExtra("flag", "edit"))
    }
    override fun onResume() {
        super.onResume()
        viewModel.CasesListSearchApi("Bearer " + userPref.getToken().toString(),  binding.edtSearch.text.toString(), "")
    }
}