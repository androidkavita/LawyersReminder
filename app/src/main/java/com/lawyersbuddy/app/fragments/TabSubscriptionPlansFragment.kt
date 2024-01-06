package com.lawyersbuddy.app.fragments


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.activities.ChooseRoleActivity
import com.lawyersbuddy.app.activities.SubscriptionPlanViewModel
import com.lawyersbuddy.app.adapter.SubscriptionPlanListAdapter
import com.lawyersbuddy.app.baseClasses.BaseFragment
import com.lawyersbuddy.app.baseClasses.BaseModel
import com.lawyersbuddy.app.databinding.DialogPaymentBinding
import com.lawyersbuddy.app.databinding.FragmentTabSubscriptionPlansBinding
import com.lawyersbuddy.app.model.SubscriptionplanData
import com.lawyersbuddy.app.util.OnItemClickListener
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class TabSubscriptionPlansFragment : BaseFragment(), View.OnClickListener,
    OnItemClickListener<BaseModel>, SubscriptionPlanListAdapter.OnClick,
    PaymentResultWithDataListener, PaymentStatusListener {
    private val viewModel: SubscriptionPlanViewModel by viewModels()
    private lateinit var subscriptionplanList: ArrayList<BaseModel>

    //    private lateinit var subscriptionPlanAdapter: SubscriptionPlanAdapter<BaseModel>
    var amount: Int = 0
    var finalPamountInt = 0
    private lateinit var subscriptionplanListt: ArrayList<BaseModel>
    private lateinit var subscriptionPlanListAdapter: SubscriptionPlanListAdapter
    lateinit var mContext: Context
    private var selectedSubscriptionPlan = ""
    private var planAmount: Int = 0
    private var subscriptionID = ""
    private var validity = ""
    private var planName = ""
    private var currentdate = ""
    private lateinit var binding: FragmentTabSubscriptionPlansBinding
    var transcId = ""
    internal val UPI_PAYMENT = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tab_subscription_plans,
            container,
            false
        )

        mContext = activity?.applicationContext!!
//        binding.backArrow.setOnClickListener(this)
        binding.llContinue.setOnClickListener(this)

        viewProgressBar()
        userPref.getToken()?.let { viewModel.subscriptionPlanApi("Bearer $it") }
        subscriptionplanList = ArrayList()
        //   subscriptionplanListt = ArrayList()
        currentdate = getCurrentDate()
        viewSubscriptionplanResponse()
        viewSubscriptionPlanDetailResponse()
        viewBuySubscriptionPlanResponse()

        viewModel.PaymentResponse.observe(viewLifecycleOwner) {
            if (it.status == 1) {
                toast(requireContext(), "Payment done")
            } else {
                toast(requireContext(), it.message)
            }
        }
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
        transcId = df.format(c)


        return binding.root
    }

    private fun viewBuySubscriptionPlanResponse() {
        /*  viewModel.buySubscriptionPlanResponse.observe(this){
              if (it.status == 1) {
                  Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
                  //  userPref.setPlanActive("1")
                  finish()
              } else if (it.status == 2) {
                  userPref.clearPref()
                  startActivity(Intent(this, LoginActivity::class.java))
                  finishAffinity()

              } else {
                  Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
              }
          }*/
    }

    private fun viewSubscriptionPlanDetailResponse() {
        viewModel.subscriptionPlanDetailResponse.observe(viewLifecycleOwner) {
            if (it.status == 1) {

                binding.tvTitle.text = it.data.title
                binding.tvDescription.text = it.data.description
                /*   subscriptionplanList.clear()
                   subscriptionplanList.addAll(it.data[0].plandetails)
                   subscriptionPlanAdapter = SubscriptionPlanAdapter(subscriptionplanList, this)
                   binding.rvSubscriptionPlan.adapter = subscriptionPlanAdapter*/
            } else {
                toast(requireContext(), it.message!!)
            }
        }
    }

    private fun viewSubscriptionplanResponse() {
        viewModel.subscriptionPlanResponse.observe(viewLifecycleOwner) {
            Log.e("@@data", it.status.toString())
            if (it.data.isNullOrEmpty()) {
                it.message?.let { utils.simpleAlert(requireContext(), "", it) }
            } else if (it.status == 2) {
                userPref.clearPref()
                startActivity(Intent(requireContext(), ChooseRoleActivity::class.java))
                activity?.finishAffinity()

            } else {
                binding.tvTitle.text = it.data[0].title
                binding.tvDescription.text = it.data[0].description
//                binding.item=it.data[0]
                subscriptionplanList.clear()
                subscriptionplanList.addAll(it.data/*[0].plandetails*/)
//                subscriptionPlanAdapter = SubscriptionPlanAdapter(subscriptionplanList, this)
//                binding.rvSubscriptionPlan.adapter = subscriptionPlanAdapter

                subscriptionPlanListAdapter = SubscriptionPlanListAdapter(it.data, this)
                binding.rvSubscriptionPlanList.adapter = subscriptionPlanListAdapter
            }

        }
    }

    private fun viewProgressBar() {
        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.llContinue -> {

                if (selectedSubscriptionPlan == "") {
                    utils.toaster("Please select any one plan before proceeding further.")
                } else {
                    val upiIntent = Intent(Intent.ACTION_VIEW)
                    val uriString =
                        "upi://pay?pa=mohdsuhel001@ybl&pn=Suhel&tr=" + subscriptionID.toString() + "&mc=1234&am=" + planAmount.toString() + "&tn=paymentForAmazon"

                    upiIntent.data = Uri.parse(uriString)
                    val chooser = Intent.createChooser(upiIntent, "Pay with...")

                    startActivityForResult(chooser, UPI_PAYMENT, null)
//                    startPayment(planAmount.toString())
//                    logoutDialog()

                }
            }
        }
    }

    private fun logoutDialog() {
        val cDialog = Dialog(requireContext(), R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogPaymentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_payment,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}
        cDialog.show()
        /* bindingDialog.btnCancel.setOnClickListener {
             cDialog.dismiss()

         }*/

        bindingDialog.send.setOnClickListener {
//            payUsingUpi(planAmount.toString(), bindingDialog.upiId.toString(), bindingDialog.name.toString(),bindingDialog.note.text.toString() /*, transcId*/)
            cDialog.dismiss()

        }

    }

    fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(activity?.packageManager!!)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(
                requireContext(),
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                 val trxt = data.getStringExtra("response")
                 Log.d("UPI", "onActivityResult: $trxt")
                 val dataList = ArrayList<String>()
                 if (trxt != null) {
                     dataList.add(trxt)
                 }
                 upiPaymentDataOperation(dataList)
             } else {
                 Log.d("UPI", "onActivityResult: " + "Return data is null")
                 val dataList = ArrayList<String>()
                 dataList.add("nothing")
                 upiPaymentDataOperation(dataList)
             }
         } else {
             Log.d("UPI", "onActivityResult: " + "Return data is null") //when user simply back without payment
             val dataList = ArrayList<String>()
             dataList.add("nothing")
             upiPaymentDataOperation(dataList)
         }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(requireContext())) {

            var str: String? = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in response.indices) {
                val equalStr =
                    response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (equalStr.size >= 0) {
                    if (equalStr[0].equals("Status", ignoreCase = true)) {
                        status = equalStr[1].lowercase(Locale.getDefault())
                    } else if (equalStr[0].equals(
                            "ApprovalRefNo",
                            ignoreCase = true
                        ) || equalStr[0].equals(
                            "txnRef", ignoreCase = true
                        )
                    ) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }

            if (status == "success") {
                //Code to handle successful transaction here.
                viewModel.PaymentApi(
                    "Bearer " + userPref.getToken().toString(),
                    subscriptionID,
                    "",
                    finalPamountInt.toString(),
                    validity,
                    currentdate
                )
                Toast.makeText(
                    requireContext(), "Transaction successful.", Toast.LENGTH_SHORT
                ).show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(requireContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected
                    && netInfo.isConnectedOrConnecting
                    && netInfo.isAvailable
                ) {
                    return true
                }
            }
            return false
        }
    }

    private fun makePayment(
        amount: String,
        upi: String,
        name: String,
        desc: String,
        transactionId: String
    ) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        val easyUpiPayment = EasyUpiPayment.Builder()
//            .with(requireActivity()) // on below line we are adding upi id.
            .setPayeeVpa(upi) // on below line we are setting name to which we are making payment.
            .setPayeeName(name) // on below line we are passing transaction id.
            .setTransactionId(transactionId) // on below line we are passing transaction ref id.
            .setTransactionRefId(transactionId) // on below line we are adding description to payment.
            .setDescription(desc) // on below line we are passing amount which is being paid.
            .setAmount(amount) // on below line we are calling a build method to build this ui.
            .build()
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment()
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this)
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        // on below line we are getting details about transaction when completed.
        val transcDetails = """
             ${transactionDetails.status.toString()}
             Transaction ID : ${transactionDetails.transactionId}
             """.trimIndent()
        viewModel.PaymentApi(
            "Bearer " + userPref.getToken().toString(),
            subscriptionID,
            transcDetails,
            finalPamountInt.toString(),
            validity,
            currentdate
        )
//        transactionDetailsTV.setVisibility(View.VISIBLE)
//        // on below line we are setting details to our text view.
//        transactionDetailsTV.setText(transcDetails)
    }

    override fun onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        toast(requireContext(), "Transaction successfully completed..")
    }

    override fun onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("TAG", "TRANSACTION SUBMIT")
    }

    override fun onTransactionFailed() {
        // this method is called when transaction is failure.
        toast(requireContext(), "Failed to complete transaction")
    }

    override fun onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        toast(requireContext(), "Transaction cancelled..")
    }

    override fun onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        toast(requireContext(), "No app found for making transaction..")
    }

    /* private fun getUPIString(
         payeeAddress: String,
         payeeName: String,
         payeeMCC: String,
         trxnID: String,
         trxnRefId: String,
         trxnNote: String,
         payeeAmount: String,
         currencyCode: String,
         refUrl: String
     ): String? {
         val UPI = ("upi://pay?pa=" + payeeAddress + "&pn=" + payeeName
                 + "&mc=" + payeeMCC + "&tid=" + trxnID + "&tr=" + trxnRefId
                 + "&tn=" + trxnNote + "&am=" + payeeAmount + "&cu=" + currencyCode
                 + "&refUrl=" + refUrl)
         return UPI.replace(" ", "+")
     }*/
    override fun onSubscriptionPlanClicked(model: SubscriptionplanData) {
        selectedSubscriptionPlan = model.days.toString()
        planName = model.days.toString()

        subscriptionID = model.id.toString()
        validity = model.days.toString()

        planAmount = (model.amount!!)


        userPref.getToken()?.let {
            viewModel.subscriptionPlanDetailApi(
                "Bearer $it",
                model.id.toString()
            )
        }

    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        TODO("Not yet implemented")
    }

    private fun startPayment(payment: String) {

        amount = (payment.replace("₹", "").toFloat() * 100).toInt()
        finalPamountInt = amount

        val activity: FragmentActivity = requireActivity()
        val co = Checkout()
        co.setKeyID("rzp_live_issQDFFwniGYsw")
        try {
            var options = JSONObject()
            options.put("name", userPref.getName())
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", finalPamountInt)
            options.put("send_sms_hash", true)

            val prefill = JSONObject()
            prefill.put("email", userPref.getEmail())
            prefill.put("contact", userPref.getmobile())
            options.put("prefill", prefill)

            co.open(requireActivity(), options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {

        viewModel.PaymentApi(
            "Bearer " + userPref.getToken().toString(),
            subscriptionID,
            p1?.paymentId.toString(),
            finalPamountInt.toString(),
            validity,
            currentdate
        )

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {

    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }

}