package com.lawyersbuddy.app.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.activities.*
import com.lawyersbuddy.app.adapter.HearingAdapter
import com.lawyersbuddy.app.adapter.SliderAdapter
import com.lawyersbuddy.app.baseClasses.BaseFragment
import com.lawyersbuddy.app.databinding.FragmentHomeBinding
import com.lawyersbuddy.app.model.BannerImg
import com.lawyersbuddy.app.viewmodel.HomeViewModel
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    var sliderDataArrayList: ArrayList<BannerImg> = ArrayList()
    private val sliderHandler: Handler = Handler()
    lateinit var viewPagerAdapter: SliderAdapter

    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        if (userPref.getType().equals("1")) {
            binding.assistant.visibility = View.VISIBLE
        } else {
            binding.assistant.visibility = View.GONE
        }
        binding.llSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        binding.llClient.setOnClickListener {
            startActivity(Intent(requireContext(), ClientListActivity::class.java).putExtra("flag","client"))
        }
        viewModel.errorString.observe(requireActivity()) {
         toast(requireActivity(),it)
        }
        binding.cases.setOnClickListener {
            startActivity(Intent(requireContext(), CaseListActivity::class.java))
        }

        binding.assistant.setOnClickListener {
            startActivity(Intent(requireContext(), AssistantActivity::class.java))

        }
        binding.llHearing.setOnClickListener {
            startActivity(Intent(requireContext(), HearingListActivity::class.java))

        }
        var page = 0
        val sliderRunnable =
            Runnable {
                binding.multiImage.setCurrentItem(page)
            }
        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
                page++
                if (position == 4) {
                    page = 0
                }
            }
        }
        binding.multiImage.registerOnPageChangeCallback(callback)

        viewModel.HomeViewModel.observe(requireActivity()) {
            if (it.status == 1) {

                sliderDataArrayList.clear()
                sliderDataArrayList = it.data.banner_img
                viewPagerAdapter = SliderAdapter(requireContext(),sliderDataArrayList)
                binding.multiImage.adapter = viewPagerAdapter

                if (sliderDataArrayList.size > 1) {
                    binding.indicator.setViewPager(binding.multiImage)
                }
                binding.clientCount.text = it.data.client_count.toString()
                binding.casesCount.text = it.data.case_count.toString()
                binding.assistantCount.text = it.data.assistant_count.toString()
                binding.hearingCount.text = it.data.hearing_count.toString()

            } else {
                Log.d("Response", it.toString())
                toast(requireContext(), it.message)
            }
        }
        viewModel.HomeApi("Bearer " + userPref.getToken().toString())
        binding.swipeRefreshlayout.setOnRefreshListener {
            viewModel.HomeApi("Bearer " + userPref.getToken().toString())
            binding.swipeRefreshlayout.isRefreshing = false
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.HomeApi("Bearer " + userPref.getToken().toString())
    }
}