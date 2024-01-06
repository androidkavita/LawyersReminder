package com.lawyersbuddy.app.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.lawyersbuddy.app.fragments.TabActivePlanFragment
import com.lawyersbuddy.app.fragments.TabOffersFragment
import com.lawyersbuddy.app.fragments.TabSubscriptionPlansFragment


class ViewPagerMySubscriptionOfferAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titles =
        //arrayOf("Last 2 days","Last 30 days","Select Date")
        arrayOf("Subscription Plans","Offers","Active Plan")

    override fun getItem(position: Int): Fragment {

        val bundle = Bundle()
        val fragment : Fragment
        return when (position) {
            0 -> {
                fragment = TabSubscriptionPlansFragment()
                bundle.putInt("type",0)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                fragment = TabOffersFragment()
                bundle.putInt("type",1)
                fragment.arguments = bundle
                fragment
            }
            else -> {
                fragment = TabActivePlanFragment()
                bundle.putInt("type",2)
                fragment.arguments = bundle
                fragment
            }
        }
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}