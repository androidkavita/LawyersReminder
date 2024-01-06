package com.lawyersbuddy.app.activities

//import com.demopush.lawyersreminder.adapter.PlacesAutoCompleteAdapter
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lawyersbuddy.app.DialogUtils
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.adapter.SearchListAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityAddNewCaseBinding
import com.lawyersbuddy.app.fragments.clicklistener.popupItemClickListnerCountry
import com.lawyersbuddy.app.model.CityListData
import com.lawyersbuddy.app.model.ClientListData
import com.lawyersbuddy.app.model.SearchResponsedataData
import com.lawyersbuddy.app.model.StateListData
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.CasesViewModel
import com.lawyersbuddy.app.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AddNewCaseActivity : BaseActivity(), DatePickerDialog.OnDateSetListener ,
    popupItemClickListnerCountry {

//    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    var click=false
    var job: Job? = null
    var name=""
    var id=""
    var client_id=0
    lateinit var no_notification: LinearLayout
    lateinit var mContext: Context

    private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    private val casesViewModel: CasesViewModel by viewModels()
    private val clientviewmodel: ClientViewModel by viewModels()
    private val CaseItemViewModel: CasesViewModel by viewModels()
    var datepicker = ""
    var clientt: ArrayList<String> = ArrayList()
    var clientt_id: ArrayList<String> = ArrayList()
    var list_name: ArrayList<SearchResponsedataData> = ArrayList()
    var clienttId = ""
    var checkdate = ""
    lateinit var clienttlist: ArrayList<ClientListData>
    var cid = ""
    var selectedCurrentDateFormat2 = ""

    var selectedHearingDateFormat1 = ""
    var first_date = ""
    var Month_DAy = 0
    var Month_year = 0
    var timeInMilliseconds: Long = 123456789
    var Year_ = 0
    var datePicker: DatePickerDialog? = null
    var startdate=""
    var hearingdate=""
    var flag = ""
    var flag1 = ""
    lateinit var binding: ActivityAddNewCaseBinding
    var cal = Calendar.getInstance()
    var cal1 = Calendar.getInstance()
    lateinit var serachAdapter:SearchListAdapter
    var mDate = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_case)
        mContext=this.applicationContext

        binding.arrow.setOnClickListener {
            finish()
        }
        if (intent != null) {
            flag = intent.getStringExtra("flag").toString()
            id = intent.getIntExtra("id", 0).toString()
        }

        clienttlist = ArrayList()
        binding.tvStartDate.text = DateFormat.date(getCurrentDate())
        CaseItemViewModel.errorString.observe(this) {
            toast(it)
        }
        binding.search.setOnClickListener {

            flag1="Clients"
            openPopUp()

        }
        if (flag.equals("edit")) {
            binding.layout.visibility = View.GONE
            CaseItemViewModel.case_detail("Bearer " + userPref.getToken().toString(), id)
            binding.clientname.visibility = View.VISIBLE
            binding.search.visibility = View.GONE
            binding.header.text = "Edit Case"
            binding.btnAddCases.text = "Edit Case"

        } else {
            binding.layout.visibility = View.VISIBLE
            binding.clientname.visibility = View.GONE
            binding.search.visibility = View.VISIBLE
            binding.header.text = "Add New Case"
            binding.btnAddCases.text = "Add Case"

        }


//        binding.actClientList.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                clienttId = clientt_id[p2]
//                clienttlist.clear()
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//        }
//




        CaseItemViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        CaseItemViewModel.CasesDetailsResponse.observe(this) {
            if (it?.status == 1) {
                try {

                    binding.etCaseTitle.setText(it.data.case_title)
                    binding.etJudgeName.setText(it.data.judge_name)
                    binding.etOpponentPartyName.setText(it.data.opponent_party_name)
                    binding.tvStartDate.text = DateFormat.NotificationDate(it.data.start_date)
                    binding.etCourtName.setText(it.data.court_name)
                    binding.etCNRnumber.setText(it.data.cnr_number)
                    binding.etCaseStatus.setText(it.data.case_status)
                    binding.etCaseStage.setText(it.data.case_stage)
                    binding.clientname.text = it.data.client_name
                    binding.etCaseDeatils.setText(it.data.case_detail)
                    binding.etOpponentPartyName.setText(it.data.opponent_party_lawyer_name.toString())
                    startdate=it.data.start_date
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                snackbar(it?.message!!)
            }
        }



        casesViewModel.addCasesResponse.observe(this) {
            if (it.status == 1) {
                snackbar(it.message.toString())
                finish()
            } else {
                snackbar(it.message.toString())
            }
        }

        binding.llStartDate.setOnClickListener {
            clickHearingDatePicker()
        }
        binding.llHearingDate.setOnClickListener {
            NextHearingDatePicker()
        }


        binding.btnAddCases.setOnClickListener {
            if (flag.equals("edit")) {
                if (binding.etCaseTitle.text.trim().isEmpty()) {
                    toast("Enter Case Title.")
                } else if (binding.tvStartDate.text.trim().isEmpty()) {
                    toast("Enter Start Date.")
//                } else if (binding.tvFirstHearingdate.text.isNullOrEmpty()) {
//                    toast("Enter First Hearing Date.")
                    /*   } else if (binding.etPartyName.text.isNullOrEmpty()){
                toast("Enter Party Name.")*/
                } else if (binding.etOpponentPartyName.text.trim().isEmpty()) {
                    toast("Enter Opponent Party Name.")
                } else if (binding.etCaseStage.text.trim().isEmpty()) {
                    toast("Enter Case Stage.")
                } else if (binding.etCaseStatus.text.trim().isEmpty()) {
                    toast("Enter Case Status.")
                } else if (binding.etJudgeName.text.trim().isEmpty()) {
                    toast("Enter Judge Name.")
                } else if (binding.etCNRnumber.text.trim().isEmpty()) {
                    toast("Enter CNR number.")
                } else if (binding.etCourtName.text.trim().isEmpty()) {
                    toast("Enter Court Name.")
                } else if (binding.etCaseDeatils.text.trim().isEmpty()) {
                    toast("Enter Case Deatils.")
                } else {

//                if (flag.equals("edit")) {
                    casesViewModel.edit_case(
                        "Bearer " + userPref.getToken().toString(),
                        id,
                        startdate,
                        binding.etOpponentPartyName.text.toString(),
                        binding.etCaseStage.text.toString(),
                        binding.etCaseStatus.text.toString(),
                        binding.etJudgeName.text.toString(),
                        binding.etCNRnumber.text.toString(),
                        binding.etCourtName.text.toString(),
                        binding.etCaseTitle.text.toString(),
                        binding.etCaseDeatils.text.toString(),
                        binding.etOpponentPartyName.text.toString()
                    )
                }

            } else {
                if (binding.etCaseTitle.text.trim().isEmpty()) {
                    toast("Enter Case Title.")
                } else if (binding.tvStartDate.text.trim().isEmpty()) {
                    toast("Enter Start Date.")
                } else if (binding.tvFirstHearingdate.text.trim().isEmpty()) {
                    toast("Enter First Hearing Date.")
                    /*  } else if (binding.actClientList.text.isNullOrEmpty()) {
                          toast("Enter Client Name.")*/
                } else if (binding.etOpponentPartyName.text.trim().isEmpty()) {
                    toast("Enter Opponent Party Name.")
                } else if (binding.etCaseStage.text.trim().isEmpty()) {
                    toast("Enter Case Stage.")
                } else if (binding.etCaseStatus.text.trim().isEmpty()) {
                    toast("Enter Case Status.")
                } else if (binding.etJudgeName.text.trim().isEmpty()) {
                    toast("Enter Judge Name.")
                } else if (binding.etCNRnumber.text.trim().isEmpty()) {
                    toast("Enter CNR number.")
                } else if (binding.etCourtName.text.trim().isEmpty()) {
                    toast("Enter Court Name.")
                } else if (binding.etCaseDeatils.text.trim().isEmpty()) {
                    toast("Enter Case Deatils.")
                } else {

                    if (startdate.equals("")){
                        startdate=getCurrentDate()
                    }else{

                    }
                    casesViewModel.add_caseApi(
                        "Bearer " + userPref.getToken().toString(),
                        startdate/*mDate*/,
                        hearingdate,
                        binding.search.text.toString(),
                        binding.etCaseStage.text.toString(),
                        binding.etCaseStatus.text.toString(),
                        binding.etJudgeName.text.toString(),
                        binding.etCNRnumber.text.toString(),
                        binding.etCourtName.text.toString(),
                        binding.etCaseTitle.text.toString(),
                        binding.etCaseDeatils.text.toString(),
                        client_id.toString(),
                        binding.etOpponentPartyName.text.toString()
                    )
                }
            }


        }

    }
    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp() {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)

            search = binding.findViewById(R.id.search_bar_edittext_popuplist)
            no_notification = binding.findViewById(R.id.no_notification)
            recyclerView.layoutManager = LinearLayoutManager(this)
//            progressbarpopup = binding.findViewById(R.id.progressbar_pop)
            var dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            var dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            var SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            SearchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(text: Editable?) {
                    filterData(text.toString(), flag1)
                }

            })
            dialougbackButton.setOnClickListener { dialog!!.dismiss() }


//            var SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {

//                    CN_SEARCH(s.toString())

                }
            })
            clientviewmodel.ClientListApi("Bearer " + userPref.getToken().toString(),"")
            clientviewmodel.ClientListResponse.observe(this) {
                if (it?.status == 1) {

                    clienttlist = it.data
                    setStateAdapter()

                } else {
                    //toast(it.message)
                    snackbar(it?.message!!)
                }
            }
//            clientviewmodel.clientsearchlist(userPref.getToken().toString(),"").observe(this) {
//                if (it. == 1) {
//
//                    clienttlist = it.data
//                    setStateAdapter()
//
//                } else {
//                    //toast(it.message)
//                    snackbar(it?.message!!)
//                }
//            }


                dialougTitle.setText("Client Name")

            dialog!!.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }



    }
    private fun filterData(searchText: String, flag1: String) {
        var filteredStateList: ArrayList<ClientListData> = ArrayList()

        if (flag1.equals("Clients")) {
            if (clienttlist != null) {
                for (item in clienttlist) {
                    try {
                        if (item.name?.toLowerCase()!!.contains(searchText.toLowerCase())) {
                            filteredStateList.add(item)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }

        try {

            serachAdapter.filterList(filteredStateList)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    fun setStateAdapter() {
        serachAdapter = this.let { SearchListAdapter(mContext, clienttlist, flag1, this) }!!
        recyclerView.adapter = serachAdapter
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickHearingDatePicker() {
        val myCalendar = Calendar.getInstance()
        var day = myCalendar.get(Calendar.DAY_OF_MONTH)
        var year = myCalendar.get(Calendar.YEAR)
        var month = myCalendar.get(Calendar.MONTH)
        first_date = day.toString() + "-" + month + 1 + "-" + year

        datePicker = DatePickerDialog(this)
        datePicker = DatePickerDialog(
            this, R.style.DatePickerTheme,
            { view, year, month, dayOfMonth -> // adding the selected date in the edittext
                Month_DAy = dayOfMonth
                Month_year = (month+1 )
                Year_ = year
                first_date = Month_DAy.toString() + "-" + Month_year + "-" + Year_
                startdate= dayOfMonth.toString() + "-" + (month+1) + "-" + year
                binding.tvStartDate.text =DateFormat.date(startdate)
//                DateFormat.getDateInstance().format(startdate)
            }, year, month, day
        )
        val inFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inFormat.parse(first_date)
        val outFormat = SimpleDateFormat("EEE MM dd HH:mm:ss z yyyy")
        val goal: String = outFormat.format(date)
        val date_test = goal
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("EEE MM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val localDate: LocalDateTime = LocalDateTime.parse(date_test, formatter)
        timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()

//        datePicker!!.datePicker.minDate = timeInMilliseconds
        datePicker!!.show()


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        if (checkdate.equals("start")) {

            cal.set(year, month, dayOfMonth)
            if (datepicker.equals("startdate")) {
                displayFormattedDate(cal.timeInMillis)
            }
        } else {
            cal1.set(year, month, dayOfMonth)
            if (datepicker.equals("startdate")) {
                displayFormattedDate(cal1.timeInMillis)
            }
        }

    }

    private fun displayFormattedDate(timeInMillis: Long) {
        val simpleDateFormat2 = android.icu.text.SimpleDateFormat("dd-MM-yyyy")
        selectedCurrentDateFormat2 = simpleDateFormat2.format(timeInMillis)
        binding.tvStartDate.text = DateFormat.date(selectedCurrentDateFormat2)

    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun NextHearingDatePicker() {
        try {

            if (first_date.equals("")) {
                first_date = getCurrentDate()
            } else {

            }

            datePicker = DatePickerDialog(this)
            datePicker = DatePickerDialog(
                this, R.style.DatePickerTheme,
                { view, Year_, Month_year, Month_DAy -> // adding the selected date in the edittext

                   hearingdate= Month_DAy.toString() + "-" + (Month_year + 1) + "-" + Year_

                    binding.tvFirstHearingdate.text =DateFormat.date(hearingdate)

                }, Year_, Month_year - 1, Month_DAy
            )


            val inFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = inFormat.parse(first_date)
            val outFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
            val goal: String = outFormat.format(date)
            val date_test = goal
            val formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val localDate: LocalDateTime = LocalDateTime.parse(date_test, formatter)
            timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()


            datePicker!!.datePicker.minDate = timeInMilliseconds
            datePicker!!.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(Date())
    }



    override fun getCountry(name: String, flag: String, id: Int) {

        binding.search.text = name
        client_id=id
        dialog?.dismiss()
    }


}