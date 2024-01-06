package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.adapter.ClientListAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.baseClasses.BaseModel
import com.lawyersbuddy.app.databinding.ActivityClientListBinding
import com.lawyersbuddy.app.model.ClientListData
import com.lawyersbuddy.app.util.ApiConstant
import com.lawyersbuddy.app.util.OnItemClickListener
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientListActivity : BaseActivity()/*,Click*/, OnItemClickListener<BaseModel>,
    View.OnClickListener {
    lateinit var binding: ActivityClientListBinding
    private val clientviewmodel: ClientViewModel by viewModels()
    private var list: ArrayList<BaseModel> = ArrayList()
    lateinit var adapter: ClientListAdapter<BaseModel>
    var job: Job? = null
    var loader=false
    var flag=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_list)
        if (intent!=null){
            flag=intent.getStringExtra("flag").toString()

        }


        progressbar()
        clientviewmodel.errorString.observe(this) {
            toast(it)
        }
        binding.addNewClient.setOnClickListener {
            startActivity(Intent(this, AddNewClientActivity::class.java))

        }
        if (userPref.getType().equals("1")) {
            binding.addclient.visibility = View.VISIBLE
        } else {
            if (userPref.getPermission().equals("0")) {
                binding.addclient.visibility = View.GONE
            } else {
                binding.addclient.visibility = View.VISIBLE

            }
        }
        binding.pulltorefresh.setOnRefreshListener {
            if (binding.edtSearch.text.trim().isEmpty()){

                clientviewmodel.ClientListApi("Bearer " + userPref.getToken().toString(), "")
                binding.pulltorefresh.isRefreshing=false

            }else{
                binding.pulltorefresh.isRefreshing=false
                binding.edtSearch.text.clear()
            }


        }

        binding.back.setOnClickListener {
            finish()
        }
        clientviewmodel.ClientListApi("Bearer " + userPref.getToken().toString(), ApiConstant.Empty)



        viewClientResponse()

        binding.edtSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500)

                it.let {
                    if (it.toString().isNotEmpty()) {

                        binding.searchImage.setImageResource(R.drawable.cross_icon)
                        clientviewmodel.ClientListSearchApi("Bearer " + userPref.getToken().toString(), binding.edtSearch.text.trim().toString())

                        binding.searchImage.setOnClickListener {

                            binding.edtSearch.text.clear()
                            clientviewmodel.ClientListSearchApi("Bearer " + userPref.getToken().toString(), "")

                        }

                    } else {

                        clientviewmodel.ClientListSearchApi("Bearer " + userPref.getToken().toString(), "")


                        binding.searchImage.setImageResource(R.drawable.icon_search)
                    }
                }
            }
        }



    }

    private fun viewClientResponse() {
        clientviewmodel.ClientListResponse.observe(this) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)
                binding.rvClientList.visibility = View.VISIBLE
                binding.tvNoEvents.visibility = View.GONE
                adapter = ClientListAdapter(list, userPref,this,/*, list,this*/)
                binding.rvClientList.adapter = adapter
                adapter.notifyDataSetChanged()

            } else {
                binding.rvClientList.visibility = View.GONE
                binding.tvNoEvents.visibility = View.VISIBLE

            }
        }
    }
 fun progressbar(){
     clientviewmodel.progressBarStatus.observe(this) {
         if (it) {
             showProgressDialog()
         } else {
             hideProgressDialog()
         }
     }
 }
    override fun onResume() {
        super.onResume()
        clientviewmodel.ClientListApi("Bearer " + userPref.getToken().toString(), ApiConstant.Empty)

    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        if (`object` is ClientListData) {
            when (view.id) {

                R.id.llViewdetails -> {

                    startActivity(
                        Intent(
                            this,
                            ClientItemDetailActivity::class.java
                        ).putExtra("id", `object`.id)
                    )
                }
                R.id.ivEdit -> {
                    val intent = Intent(this, AddNewClientActivity::class.java)
                    intent.putExtra("type", "edit")
                    intent.putExtra("id", `object`.id)
                    intent.putExtra("name", `object`.name)
                    intent.putExtra("mobile", `object`.mobile)
                    intent.putExtra("email", `object`.email)
                    intent.putExtra("address", `object`.address)
                    intent.putExtra("image", `object`.image)
                    startActivity(intent)

                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

}