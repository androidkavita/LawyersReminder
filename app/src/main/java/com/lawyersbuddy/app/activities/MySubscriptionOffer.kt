package com.lawyersbuddy.app.activities

import android.os.Bundle
import com.lawyersbuddy.app.adapter.ViewPagerMySubscriptionOfferAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityMySubscriptionOfferBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySubscriptionOffer : BaseActivity() {

    lateinit var _binding: ActivityMySubscriptionOfferBinding
    private var payoutReportPagerAdapter: ViewPagerMySubscriptionOfferAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMySubscriptionOfferBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.arrow.setOnClickListener {
            finish()
        }
        setTab()
    }
    private fun setTab() {
        payoutReportPagerAdapter = ViewPagerMySubscriptionOfferAdapter(supportFragmentManager)
        _binding.viewPager.adapter = payoutReportPagerAdapter
        _binding.tabLayout.setupWithViewPager(_binding.viewPager)


        _binding.tabLayout.getChildAt(0)

        _binding.viewPager.adapter = payoutReportPagerAdapter
        _binding.tabLayout.setupWithViewPager(_binding.viewPager)
    }

}