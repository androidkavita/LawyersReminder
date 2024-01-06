package com.lawyersbuddy.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.adapter.RecyclerViewAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.baseClasses.BaseModel
import com.lawyersbuddy.app.databinding.ActivityAssistantBinding
import com.lawyersbuddy.app.model.AssistantListData
import com.lawyersbuddy.app.util.OnItemClickListener
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.AssistantViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssistantActivity : BaseActivity(), OnItemClickListener<BaseModel>, View.OnClickListener {

    private lateinit var binding: ActivityAssistantBinding
    private val Assistantviewmodel: AssistantViewModel by viewModels()
    private var list: ArrayList<BaseModel> = ArrayList()
    lateinit var adapter: RecyclerViewAdapter<BaseModel>
    var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_assistant)

        adapter = RecyclerViewAdapter(list, this)
        adapter.mActivity = this
        Assistantviewmodel.AssistantListApi("Bearer " + userPref.getToken().toString(), "")

        binding.addNewClient.setOnClickListener {
            startActivity(Intent(this, com.lawyersbuddy.app.activities.AddNewAssistantActivity::class.java))
        }
        viewProgressStatus()
        Assistantviewmodel.errorString.observe(this) {
            toast(it)
        }
        binding.pulltorefresh.setOnRefreshListener {
            if (binding.edtSearch.text.trim().isEmpty()){
                binding.pulltorefresh.isRefreshing=false

                Assistantviewmodel.AssistantListApi("Bearer " + userPref.getToken().toString(), "")
            }else{
                binding.pulltorefresh.isRefreshing=false
                binding.edtSearch.text.clear()
            }


        }

        binding.arrow.setOnClickListener {
            finish()
        }


        Assistantviewmodel.AssistantListResponse.observe(this) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)
                binding.rvAssistant.visibility = View.VISIBLE
                binding.tvNoEvents.visibility = View.GONE
                binding.rvAssistant.adapter = adapter
                adapter.notifyDataSetChanged()

            } else {
                binding.rvAssistant.visibility = View.GONE
                binding.tvNoEvents.visibility = View.VISIBLE
            }
        }

        binding.edtSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500)

                it.let {
                    if (it.toString().isNotEmpty()) {
                        Assistantviewmodel.AssistantListSearchApi("Bearer " + userPref.getToken().toString(), binding.edtSearch.text.toString())

                        binding.searchImage.setImageResource(R.drawable.cross_icon)
                        binding.searchImage.setOnClickListener {
                            binding.edtSearch.text.clear()
                            Assistantviewmodel.AssistantListSearchApi("Bearer " + userPref.getToken().toString(), "")

                        }

                    } else {
                        binding.searchImage.setImageResource(R.drawable.icon_search)
                        Assistantviewmodel.AssistantListSearchApi("Bearer " + userPref.getToken().toString(),"")

                    }
                }
            }
        }

    }
    private fun viewProgressStatus() {
        Assistantviewmodel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        Assistantviewmodel.AssistantListSearchApi("Bearer " + userPref.getToken().toString(), "")

    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        if (`object` is AssistantListData) {
            when (view.id) {

                R.id.llViewdetails -> {

                    startActivity(
                        Intent(
                            this,
                            AssistantDetailActivity::class.java
                        ).putExtra("id", `object`.id)
                    )
                }
                R.id.ivEdit -> {
                    val intent = Intent(this, com.lawyersbuddy.app.activities.AddNewAssistantActivity::class.java)
                    intent.putExtra("type", "edit")
                    intent.putExtra("id", `object`.id)
                    intent.putExtra("name", `object`.name)
                    intent.putExtra("mobile", `object`.mobile)
                    intent.putExtra("email", `object`.email)
                    intent.putExtra("image", `object`.image)
                    intent.putExtra("permission", `object`.is_permission)
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