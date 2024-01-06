package com.lawyersbuddy.app.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyersbuddy.app.activities.HearingDetailsActivity
import com.lawyersbuddy.app.adapter.TodayCalendarAdapter
import com.lawyersbuddy.app.baseClasses.BaseFragment
import com.lawyersbuddy.app.fragments.clicklistener.Click
import com.lawyersbuddy.app.databinding.FragmentTodayCalendarBinding
import com.lawyersbuddy.app.model.CalenderListData
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.CalenderViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class TodayCalendarFragment : BaseFragment(), Click, DatePickerDialog.OnDateSetListener {
    private val viewModel: CalenderViewModel by viewModels()
    lateinit var binding: FragmentTodayCalendarBinding
    private var list: ArrayList<CalenderListData> = ArrayList()
    lateinit var adapter: TodayCalendarAdapter
    var downloadUrl=""
    val current = LocalDate.now()
    var cal = Calendar.getInstance()
    var mDate = ""
    var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodayCalendarBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        viewModel.CalenderListApi("Bearer " + userPref.getToken().toString(), getCurrentDate())

        binding.date.text = DateFormat.date( getCurrentDate())

        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        viewModel.CasesListResponse.observe(viewLifecycleOwner) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)
                binding.rvTodayCal.visibility = View.VISIBLE
                binding.tvNoEvents.visibility = View.GONE

                binding.rvTodayCal.layoutManager = LinearLayoutManager(activity)
                adapter = TodayCalendarAdapter(requireContext(), list, this)

                binding.rvTodayCal.adapter = adapter
                adapter.notifyDataSetChanged()

            } else {
                binding.rvTodayCal.visibility = View.GONE
                binding.tvNoEvents.visibility = View.VISIBLE
//                toast(requireContext(),it?.message!!)
            }
        }

        binding.llCalendar.setOnClickListener {
            clickStartDatePicker()
        }
        binding.save.setOnClickListener {
            viewModel.download_all_events("Bearer " + userPref.getToken().toString(),mDate,userPref.getid().toString())
        }
       viewModel.DownloadTodayEventResponse.observe(viewLifecycleOwner){
          if (it.status==1){
              downloadUrl= it.data.toString()
              startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)))
              toast(requireContext(),"File download successfully.")
          }
           else{

          }
       }
        return view
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickStartDatePicker() {
        DatePickerDialog(
            requireContext(), this, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun click(id: String) {
        startActivity(
            Intent(
                requireContext(),
                HearingDetailsActivity::class.java
            ).putExtra("event_id", id).putExtra("flag", "calender")
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year, month, dayOfMonth)
        displayFormattedDate(cal.timeInMillis)
    }

    private fun displayFormattedDate(timeInMillis: Long) {
        mDate = simpleDateFormat.format(timeInMillis)
        binding.date.text =DateFormat.date( mDate)

        viewModel.CalenderListApi(
            "Bearer " + userPref.getToken().toString(),
            mDate
        )

    }


}